package com.education.service;

import com.education.db.entity.BackendRoleEntity;
import com.education.db.jpa.BackendRoleRepository;
import com.education.formbean.BackendRoleBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yzzhao on 12/26/15.
 */
@Service("BackendRoleService")
public class BackendRoleService {
    @Autowired
    private BackendRoleRepository roleRepository;

    public int createNewRole(String roleName) {
        BackendRoleEntity entity = new BackendRoleEntity();
        entity.setRole(roleName);
        BackendRoleEntity save = roleRepository.save(entity);
        return save.getId();
    }

    public List<String> getRoleNames() {
        return roleRepository.getAllRoleNames();
    }

    public List<BackendRoleBean> getRoles() {
        Iterable<BackendRoleEntity> roles = roleRepository.findAll();
        List<BackendRoleBean> beans = new ArrayList<>();
        for (BackendRoleEntity entity : roles) {
            BackendRoleBean bean = new BackendRoleBean(entity.getId(), entity.getRole());
            beans.add(bean);
        }
        return beans;
    }
}
