package com.education.service;

import com.education.db.entity.COURSE_ACCESS_FLAG;
import com.education.db.entity.LoginHistoryEntity;
import com.education.db.entity.UserEntity;
import com.education.db.jpa.LoginHistoryRepository;
import com.education.db.jpa.UserRepository;
import com.education.exception.BadRequestException;
import com.education.exception.ErrorCode;
import com.education.formbean.UserCourseHistoryBean;
import com.education.service.converter.OnlineUserInfoConverter;
import com.education.service.converter.WeChatUserConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.function.Consumer;

/**
 * Created by yzzhao on 1/15/16.
 */
@Service("OnlineUserService")
public class BackendOnlineUserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private WeChatUserConverter weChatUserConverter;

    @Autowired
    private UserCourseHistoryService historyService;

    @Autowired
    private LoginHistoryService loginHistoryService;

    @Autowired
    private OnlineUserInfoConverter userInfoConverter;

    public List<OnlineUserInfo> getUserList(int pageIdx, int number) {
        Page<UserEntity> entities = userRepository.findAll(createPagable(pageIdx, number));
        List<OnlineUserInfo> userInfoList = new ArrayList<>();
        Page<WeChatUserInfo> map = entities.map(weChatUserConverter);
        map.forEach(new Consumer<WeChatUserInfo>() {
            @Override
            public void accept(WeChatUserInfo userInfo) {
                OnlineUserInfo onlineUser = userInfoConverter.convert(userInfo);
                Date loginTime = getUserLastLoginTime(userInfo.getUserId());
                onlineUser.setLastLoginTime(loginTime);
                userInfoList.add(onlineUser);
            }
        });
        return userInfoList;
    }

    public long getUserCount() {
        return userRepository.count();
    }

    public List<UserCourseHistoryBean> getUserAccessCourseHistory(int userId, int pageIdx,
                                                                  int number, String flag) {
        COURSE_ACCESS_FLAG courseAccessFlag = null;
        if(flag != null){
            try {
                courseAccessFlag = COURSE_ACCESS_FLAG.valueOf(flag);
            } catch (IllegalArgumentException e) {
            }
        }
        return historyService.getUserAccessHistory(userId, pageIdx, number, courseAccessFlag);
    }

    public int getUserAccessCount(int userId){
        return historyService.getUesrAccessCount(userId);
    }

    public Date getUserLastLoginTime(int userId){
        UserEntity user = userRepository.findOne(userId);
        if(user == null){
            throw new BadRequestException(ErrorCode.NOT_LOGIN);
        }
        LoginHistoryEntity lastLoginTime = loginHistoryService.getLastLoginTime(user.getUserId());
        if(lastLoginTime != null){
            return lastLoginTime.getLoginTime();
        }
        return null;
    }

    private Pageable createPagable(int pageIdx, int number) {
        return new PageRequest(pageIdx, number, new Sort(Sort.Direction.DESC, "userId"));
    }
}
