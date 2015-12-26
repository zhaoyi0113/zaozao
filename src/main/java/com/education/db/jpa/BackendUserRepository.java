package com.education.db.jpa;

import com.education.db.entity.BackendRoleEntity;
import com.education.db.entity.BackendUserEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Created by yzzhao on 12/26/15.
 */
public interface BackendUserRepository extends CrudRepository<BackendUserEntity, Integer>{

    List<BackendUserEntity> findByNameAndPassword(String name , String password);

    List<BackendUserEntity> findByName(String name);
}
