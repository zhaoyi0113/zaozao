package com.education.db.jpa;

import com.education.db.entity.UserCourseHistoryEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

/**
 * Created by yzzhao on 1/9/16.
 */
public interface UserCourseHistoryRepository extends PagingAndSortingRepository<UserCourseHistoryEntity, Long> {

    List<UserCourseHistoryEntity> findByUserId(int userId);

    List<UserCourseHistoryEntity> findByCourseId(int courseId);

    List<UserCourseHistoryEntity> findByCourseId(int courseId, Pageable pageable);

    List<UserCourseHistoryEntity> findByCourseIdOrderByTimeCreatedDesc(int courseId);

    List<UserCourseHistoryEntity> findByUserIdAndAccessFlag(int userId, String flag, Pageable pageable);

    List<UserCourseHistoryEntity> findByUserId(int userId, Pageable pageable);

}
