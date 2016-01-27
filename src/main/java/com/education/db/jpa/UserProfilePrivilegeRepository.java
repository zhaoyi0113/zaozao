package com.education.db.jpa;

import com.education.db.entity.UserProfilePrivilegeEntity;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by yzzhao on 1/27/16.
 */
public interface UserProfilePrivilegeRepository extends CrudRepository<UserProfilePrivilegeEntity, Integer> {
}
