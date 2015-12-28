package com.education.db.jpa;

import com.education.db.entity.BackendRoleEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Created by yzzhao on 12/26/15.
 */
public interface BackendRoleRepository extends CrudRepository<BackendRoleEntity, Integer> {
    @Query("select role from BackendRoleEntity")
    List<String> getAllRoleNames();
}
