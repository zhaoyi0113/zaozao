package com.education.service;

import com.education.db.entity.LoginHistoryEntity;
import com.education.db.entity.UserEntity;
import com.education.db.jpa.UserRepository;
import com.education.exception.BadRequestException;
import com.education.exception.ErrorCode;
import com.education.formbean.UserChildrenRegisterBean;
import com.education.service.converter.WeChatUserConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.logging.Logger;

/**
 * Created by yzzhao on 1/24/16.
 */
@Service("UserService")
public class UserService {

    private static Logger logger = Logger.getLogger(UserService.class.getName());

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private WeChatUserConverter weChatUserConverter;

    @Autowired
    private LoginHistoryService historyService;

    @Autowired
    private ParentService parentService;

    public WeChatUserInfo getUserInfo(String token){
        UserEntity userEntity = historyService.getUserByToken(token);
        if(userEntity == null){
            logger.severe("not find user from token "+token);
            throw new BadRequestException(ErrorCode.NOT_LOGIN);
        }
        return getUserInfo(userEntity.getUserId());
    }

    public WeChatUserInfo getUserInfo(int userId){
        UserEntity user = userRepository.findOne(userId);
        if(user == null){
            logger.severe("can't find user by id "+userId);
            throw new BadRequestException(ErrorCode.INVALID_USER);
        }
        WeChatUserInfo userInfo = weChatUserConverter.convert(user);
        UserChildrenRegisterBean child = parentService.getUserChild(userInfo.getUserId());
        userInfo.setChild(child);
        logger.info("get user info "+userInfo);
        return userInfo;
    }
}
