package com.education.service;

import com.education.auth.Login;
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
@Login
public class BackendUserService {

    @Autowired
    private BackendUserRepository userRepository;

    @Autowired
    private BackendRoleRepository roleRepository;

    public int createNewUser(BackendUserBean userBean) {
        BackendRoleEntity role = roleRepository.findOne(userBean.getRoleId());
        if (role == null) {
            throw new BadRequestException(ErrorCode.ROLE_NOT_FOUND);
        }
        BackendUserEntity userEntity = createUserEntity(userBean);
        BackendUserEntity save = userRepository.save(userEntity);
        return save.getId();
    }

    public List<BackendUserBean> getAllUsers() {
        Iterable<BackendUserEntity> userEntities = userRepository.findAll();
        List<BackendUserBean> userBeanList = new ArrayList<>();
        for (BackendUserEntity entity : userEntities) {
            BackendUserBean userBean = createUserBean(entity);

            userBeanList.add(userBean);
        }
        return userBeanList;
    }

    public BackendUserBean queryUser(int id) {
        BackendUserEntity user = this.userRepository.findOne(id);
        return createUserBean(user);
    }

    public void deleteUser(int id) {
        userRepository.delete(id);
    }

    public void editUser(BackendUserBean userBean) {
        BackendUserEntity user = this.userRepository.findOne(userBean.getId());
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
        this.userRepository.save(user);
    }

    private BackendUserBean createUserBean(BackendUserEntity userEntity) {
        BackendUserBean userBean = new BackendUserBean();
        userBean.setId(userEntity.getId());
        userBean.setName(userEntity.getName());
        userBean.setRoleId(userEntity.getRoleId());
        userBean.setEmail(userEntity.getEmail());
        BackendRoleEntity role = roleRepository.findOne(userEntity.getRoleId());
        userBean.setRole(role.getRole());
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
