package com.education.service;

import com.education.db.entity.BackendRoleEntity;
import com.education.db.entity.BackendUserEntity;
import com.education.db.jpa.BackendRoleRepository;
import com.education.db.jpa.BackendUserRepository;
import com.education.exception.BadRequestException;
import com.education.exception.ErrorCode;
import com.education.formbean.BackendUserBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yzzhao on 12/26/15.
 */
@Service("BackendUserService")
public class BackendUserService {

    @Autowired
    private BackendUserRepository backendUserRepository;

    @Autowired
    private BackendRoleRepository backendRoleRepository;

    public int createNewUser(BackendUserBean userBean) {
        BackendRoleEntity role = backendRoleRepository.findOne(userBean.getRoleId());
        if (role == null) {
            throw new BadRequestException(ErrorCode.ROLE_NOT_FOUND);
        }
        BackendUserEntity userEntity = createUserEntity(userBean);
        BackendUserEntity save = backendUserRepository.save(userEntity);
        return save.getId();
    }

    public List<BackendUserBean> getAllUsers() {
        Iterable<BackendUserEntity> userEntities = backendUserRepository.findAll();
        List<BackendUserBean> userBeanList = new ArrayList<>();
        for (BackendUserEntity entity : userEntities) {
            BackendUserBean userBean = createUserBean(entity);
            userBeanList.add(userBean);
        }
        return userBeanList;
    }

    public BackendUserBean queryUser(int id) {
        BackendUserEntity user = backendUserRepository.findOne(id);
        return createUserBean(user);
    }

    public void deleteUser(int id) {
        backendUserRepository.delete(id);
    }

    public void editUser(BackendUserBean userBean) {
        BackendUserEntity user = backendUserRepository.findOne(userBean.getId());
        if (user == null) {
            throw new BadRequestException(ErrorCode.USER_NOT_EXISTED);
        }
        if (userBean.getEmail() != null) {
            user.setEmail(userBean.getEmail());
        }
        if (userBean.getName() != null) {
            user.setName(userBean.getName());
        }
        if (userBean.getPassword() != null) {
            user.setPassword(userBean.getPassword());
        }
        if (userBean.getRoleId() != 0) {
            user.setRoleId(userBean.getRoleId());
        }
        backendUserRepository.save(user);
    }

    private static BackendUserBean createUserBean(BackendUserEntity userEntity) {
        BackendUserBean userBean = new BackendUserBean();
        userBean.setId(userEntity.getId());
        userBean.setName(userEntity.getName());
        userBean.setRoleId(userEntity.getRoleId());
        userBean.setEmail(userEntity.getEmail());
        return userBean;
    }

    private static BackendUserEntity createUserEntity(BackendUserBean userBean) {
        BackendUserEntity entity = new BackendUserEntity();
        entity.setName(userBean.getName());
        entity.setEmail(userBean.getEmail());
        entity.setPassword(userBean.getPassword());
        entity.setRoleId(userBean.getRoleId());
        return entity;
    }
}
