package com.education.db.jpa;

import com.education.db.entity.UserCourseHistoryEntity;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by yzzhao on 1/9/16.
 */
public interface UserCourseHistoryRepository extends CrudRepository<UserCourseHistoryEntity, Long>{
}
