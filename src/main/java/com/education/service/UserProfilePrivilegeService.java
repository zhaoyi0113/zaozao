package com.education.service;

import com.education.db.entity.UserProfilePrivilegeEntity;
import com.education.db.jpa.UserProfilePrivilegeRepository;
import com.education.exception.BadRequestException;
import com.education.exception.ErrorCode;
import com.education.formbean.UserProfilePrivilegeBean;
import com.education.service.converter.UserProfilePrivilegeBeanConverter;
import jersey.repackaged.com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by yzzhao on 1/27/16.
 */
@Service("UserProfilePrivilegeService")
public class UserProfilePrivilegeService {

    @Autowired
    private UserProfilePrivilegeRepository repository;

    @Autowired
    private UserProfilePrivilegeBeanConverter converter;

    @Transactional
    public void editUserProfilePrivilege(UserProfilePrivilegeBean bean) {
        UserProfilePrivilegeEntity entity = findEntity();
        entity.setChildBirthdate(bean.getChildBirthdate());
        entity.setUserName(bean.getUserName());
        entity.setUserImage(bean.getUserImage());
        entity.setChildName(bean.getChildName());
    }

    public UserProfilePrivilegeBean getUserProfilePrivilege() {

        UserProfilePrivilegeEntity entity = findEntity();
        return converter.convert(entity);
    }

    private UserProfilePrivilegeEntity findEntity(){
        Iterable<UserProfilePrivilegeEntity> all = repository.findAll();
        List<UserProfilePrivilegeEntity> list = Lists.newArrayList(all);
        if (list.isEmpty()) {
            throw new BadRequestException(ErrorCode.UNKNOWN);
        }
        return list.get(0);
    }
}
