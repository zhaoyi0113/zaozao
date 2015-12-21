package com.education.service;

import com.education.db.entity.LoginHistoryEntity;
import com.education.db.entity.UserEntity;
import com.education.db.jpa.LoginHistoryRepository;
import com.education.db.jpa.UserRepository;
import com.education.exception.BadRequestException;
import com.education.exception.ErrorCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by yzzhao on 12/21/15.
 */
@Service("LoginHistory")
public class LoginHistoryService {

    @Autowired
    private LoginHistoryRepository loginHistoryRepository;

    @Autowired
    private UserRepository userRepository;

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
}
