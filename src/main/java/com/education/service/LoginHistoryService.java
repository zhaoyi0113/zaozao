package com.education.service;

import com.education.db.entity.LoginHistoryEntity;
import com.education.db.entity.UserEntity;
import com.education.db.jpa.LoginHistoryRepository;
import com.education.db.jpa.UserRepository;
import com.education.exception.BadRequestException;
import com.education.exception.ErrorCode;
import com.education.util.AccessTokenGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

/**
 * Created by yzzhao on 12/21/15.
 */
@Service("LoginHistory")
public class LoginHistoryService {

    private static final Logger logger = Logger.getLogger(LoginHistoryService.class.getName());

    @Autowired
    private LoginHistoryRepository loginHistoryRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired AccessTokenGenerator tokenGenerator;

    @Transactional
    public String saveWeChatUserLogin(WeChatUserInfo userInfo, String source) {
        int userId = -1;
        List<UserEntity> users = userRepository.findByUnionid(userInfo.getUnionid());
        if (users.isEmpty()) {
            userId = saveUserInfo(userInfo);
        } else {
            UserEntity userEntity = users.get(0);
            userId = userEntity.getUserId();
        }
        LoginHistoryEntity entity = new LoginHistoryEntity();
        entity.setLoginTime(Calendar.getInstance().getTime());
        entity.setUserid(userId);
        String accessToken = tokenGenerator.generateAccessToken(userInfo.getUnionid(), System.currentTimeMillis() + "");
        entity.setToken(accessToken);
        entity.setSource(source);
        LoginHistoryEntity save = loginHistoryRepository.save(entity);
        return save.getToken();
    }

    private int saveUserInfo(WeChatUserInfo userInfo) {
        UserEntity entity = new UserEntity();
        entity.setGroupid(userInfo.getGroupid());
        entity.setRemark(userInfo.getRemark());
        entity.setCity(userInfo.getCity());
        entity.setCountry(userInfo.getCountry());
        entity.setLanguage(userInfo.getLanguage());
        entity.setHeadimageurl(userInfo.getHeadimgurl());
        entity.setOpenid(userInfo.getOpenid());
        entity.setSex(userInfo.getSex());
        entity.setNickname(userInfo.getNickname());
        entity.setUserName(userInfo.getNickname());
        entity.setProvince(userInfo.getProvince());
        entity.setSubscribe(userInfo.getSubscribe());
        entity.setUnionid(userInfo.getUnionid());
        entity.setSubscribe_time(userInfo.getSubscribe_time());
        UserEntity saved = userRepository.save(entity);
        return saved.getUserId();
    }

    public int saveLogin(int userid) {
        UserEntity user = userRepository.findOne(userid);
        if (user == null) {
            throw new BadRequestException(ErrorCode.NOT_LOGIN);
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        Date time = calendar.getTime();
        LoginHistoryEntity loginEntity = new LoginHistoryEntity();
        loginEntity.setUserid(user.getUserId());
        loginEntity.setLoginTime(time);
        LoginHistoryEntity saved = loginHistoryRepository.save(loginEntity);
        return saved.getId();
    }

    public LoginHistoryEntity getLastLoginTime(int userId) {
        List<LoginHistoryEntity> loginHis = loginHistoryRepository.findByUseridOrderByLoginTimeDesc(userId);
        if (loginHis != null && loginHis.size() > 0) {
            return loginHis.get(0);
        }
        return null;
    }

    public int getLoginCount(int userId) {
        return loginHistoryRepository.countByuserid(userId);
    }

    public int getDayNumber(int userId) {
        List<LoginHistoryEntity> userList = loginHistoryRepository.findByUseridOrderByLoginTimeAsc(userId);
        if (userList == null || userList.size() <= 0) {
            return 0;
        }
        int dayNumber = 1;
        LoginHistoryEntity preUser = null;
        for (int i = 0; i < userList.size(); i++) {
            if (preUser == null) {
                preUser = userList.get(i);
                continue;
            }
            Calendar preCalendar = Calendar.getInstance();
            preCalendar.setTime(preUser.getLoginTime());
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(userList.get(i).getLoginTime());
            if (preCalendar.get(Calendar.DAY_OF_MONTH) != calendar.get(Calendar.DAY_OF_MONTH)
                    || preCalendar.get(Calendar.MONTH) != calendar.get(Calendar.MONTH)
                    || preCalendar.get(Calendar.YEAR) != calendar.get(Calendar.YEAR)) {
                dayNumber++;
            }
            preUser = userList.get(i);
        }
        return dayNumber;
    }

    public UserEntity getUserByToken(String token){
        List<LoginHistoryEntity> tokenList = loginHistoryRepository.findByToken(token);
        if(tokenList == null || tokenList.isEmpty()){
            logger.severe("can't find user from token "+token);
            return null;
        }
        UserEntity user = userRepository.findOne(tokenList.get(0).getUserid());
        return user;
    }
}
