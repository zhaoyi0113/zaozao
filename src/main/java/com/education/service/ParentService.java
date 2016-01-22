package com.education.service;

import com.education.db.entity.ChildrenEntity;
import com.education.db.entity.UserEntity;
import com.education.db.jpa.ChildrenRepository;
import com.education.db.jpa.UserRepository;
import com.education.exception.BadRequestException;
import com.education.exception.ErrorCode;
import com.education.formbean.UserChildrenRegisterBean;
import com.education.service.converter.WeChatUserConverter;
import com.education.ws.util.WSUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by yzzhao on 12/15/15.
 */
@Service("ParentService")
public class ParentService {

    private static final Logger logger = Logger.getLogger(ParentService.class.getName());

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ChildrenRepository childrenRepository;

    @Transactional
    public int registerUserChild(UserChildrenRegisterBean bean, WeChatUserInfo userInfo) {
        List<UserEntity> userEntities = userRepository.findByUnionid(userInfo.getUnionid());
        if (userEntities.isEmpty()) {
            throw new BadRequestException(ErrorCode.NOT_LOGIN);
        }
        UserEntity userEntity = userEntities.get(0);
        ChildrenEntity childrenEntity = createChildrenEntity(bean, userEntity.getUserId());
        ChildrenEntity save = childrenRepository.save(childrenEntity);
        return save.getId();
    }

    @Transactional
    public void editUserChild(UserChildrenRegisterBean bean) {
        ChildrenEntity entity = childrenRepository.findOne(bean.getId());
        if (entity == null) {
            throw new BadRequestException(ErrorCode.COMMON_NOT_FOUND);
        }
        entity.setAge(bean.getAge());
        entity.setGender(bean.getGender());
        entity.setBirthdate(WSUtility.stringToDate(bean.getChildBirthdate()));
        entity.setName(bean.getChildName());

        childrenRepository.save(entity);
    }

    @Transactional
    public void deleteUserChild(WeChatUserInfo userInfo){
        List<UserEntity> userEntities = userRepository.findByUnionid(userInfo.getUnionid());
        if (userEntities.isEmpty()) {
            throw new BadRequestException(ErrorCode.NOT_LOGIN);
        }
        childrenRepository.deleteByParentId(userEntities.get(0).getUserId());
    }

    public UserChildrenRegisterBean getUserChild(WeChatUserInfo userInfo) {
        List<UserEntity> userEntities = userRepository.findByUnionid(userInfo.getUnionid());
        if (userEntities.isEmpty()) {
            throw new BadRequestException(ErrorCode.NOT_LOGIN);
        }
        List<ChildrenEntity> children = childrenRepository.findByParentId(userEntities.get(0).getUserId());
        if (children.isEmpty()) {
            throw new BadRequestException(ErrorCode.COMMON_NOT_FOUND);
        }
        UserChildrenRegisterBean bean = new UserChildrenRegisterBean();
        bean.setId(children.get(0).getId());
        bean.setChildBirthdate(WSUtility.dateToString(children.get(0).getBirthdate()));
        bean.setChildName(children.get(0).getName());
        bean.setGender(children.get(0).getGender());
        bean.setAge(children.get(0).getAge());
        return bean;
    }

    private ChildrenEntity createChildrenEntity(UserChildrenRegisterBean bean, int parentId) {
        ChildrenEntity entity = new ChildrenEntity();
        entity.setName(bean.getChildName());
        try {
            Date date = WSUtility.stringToDate(bean.getChildBirthdate());
            entity.setBirthdate(date);
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
            throw new BadRequestException(ErrorCode.REGISTER_NEW_MEMBER_FAILED);
        }
        entity.setGender(bean.getGender());
        entity.setParentId(parentId);
        entity.setAge(bean.getAge());
        return entity;
    }

}
