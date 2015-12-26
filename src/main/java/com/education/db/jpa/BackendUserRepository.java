package com.education.db.jpa;

import com.education.db.entity.BackendUserEntity;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by yzzhao on 12/26/15.
 */
public interface BackendUserRepository extends CrudRepository<BackendUserEntity, Integer>{
}
