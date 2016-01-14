package com.education.db.jpa;

import com.education.db.entity.UserCourseHistoryEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Created by yzzhao on 1/9/16.
 */
public interface UserCourseHistoryRepository extends CrudRepository<UserCourseHistoryEntity, Long>{

    List<UserCourseHistoryEntity> findByUserId(int userId);

    List<UserCourseHistoryEntity> findByCourseId(int courseId);
}
