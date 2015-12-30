package com.education.service;

import com.education.db.entity.ChildrenEntity;
import com.education.db.entity.UserEntity;
import com.education.db.jpa.ChildrenRepository;
import com.education.db.jpa.UserRepository;
import com.education.exception.BadRequestException;
import com.education.exception.ErrorCode;
import com.education.formbean.UserChildrenRegisterBean;
import com.education.ws.util.WSUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by yzzhao on 12/15/15.
 */
@Service("UserService")
public class UserService {

    private static final Logger logger = Logger.getLogger(UserService.class.getName());

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ChildrenRepository childrenRepository;

    @Transactional
    public void registerUserChild(UserChildrenRegisterBean bean, WeChatUserInfo userInfo) {
        UserEntity userEntity = createUserEntity(userInfo);
        UserEntity savedEntity = userRepository.save(userEntity);
        ChildrenEntity childrenEntity = createChildrenEntity(bean, savedEntity.getUserId());
        childrenRepository.save(childrenEntity);
    }

    private ChildrenEntity createChildrenEntity(UserChildrenRegisterBean bean, int parentId){
        ChildrenEntity entity = new ChildrenEntity();
        entity.setName(bean.getChildName());
        try {
            Date date = WSUtility.stringToDate(bean.getChildBirthdate());
            entity.setBirthdate(date);
        }catch(Exception e){
            logger.log(Level.SEVERE, e.getMessage(), e);
            throw new BadRequestException(ErrorCode.REGISTER_NEW_MEMBER_FAILED);
        }
        entity.setGender(bean.getGender());
        entity.setParentId(parentId);
        return entity;
    }

    private UserEntity createUserEntity( WeChatUserInfo userInfo) {
        UserEntity entity = new UserEntity();
        entity.setSubscribe(userInfo.getSubscribe());
        entity.setOpenid(userInfo.getOpenid());
        entity.setNickname(userInfo.getNickname());
        entity.setCity(userInfo.getCity());
        entity.setSex(userInfo.getSex());
        entity.setLanguage(userInfo.getSex());
        entity.setProvince(userInfo.getProvince());
        entity.setCountry(userInfo.getCountry());
        entity.setHeadimageurl(userInfo.getHeadimgurl());
        entity.setSubscribe_time(userInfo.getSubscribe_time());
        entity.setUnionid(userInfo.getUnionid());
        entity.setRemark(userInfo.getRemark());
        entity.setGroupid(userInfo.getGroupid());
        return entity;
    }

}
