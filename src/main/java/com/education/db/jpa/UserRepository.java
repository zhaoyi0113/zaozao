package com.education.db.jpa;

import com.education.db.entity.UserEntity;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

/**
 * Created by yzzhao on 11/22/15.
 */
public interface UserRepository extends PagingAndSortingRepository<UserEntity, Integer> {

    List<UserEntity> findByUserName(String userName);

    List<UserEntity> findByUnionid(String unionId);
}
