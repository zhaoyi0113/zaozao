package com.education.db.jpa;

import com.education.db.entity.UserEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Created by yzzhao on 11/22/15.
 */
public interface UserRepository extends CrudRepository<UserEntity, Integer> {

    List<UserEntity> findByUserName(String userName);

    List<UserEntity> findByOpenid(String openid);
}
