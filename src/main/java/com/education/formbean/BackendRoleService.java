package com.education.formbean;

import com.education.db.entity.BackendRoleEntity;
import com.education.db.jpa.BackendRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by yzzhao on 12/26/15.
 */
@Service("BackendRoleService")
public class BackendRoleService {
    @Autowired
    private BackendRoleRepository roleRepository;

    public int createNewRole(String roleName){
        BackendRoleEntity entity = new BackendRoleEntity();
        entity.setRole(roleName);
        BackendRoleEntity save = roleRepository.save(entity);
        return save.getId();
    }
}
