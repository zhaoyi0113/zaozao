package com.education.db.jpa;

import com.education.db.entity.CourseEntity;
import com.education.db.entity.UserFavoriteEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Created by yzzhao on 1/22/16.
 */
public interface UserFavoriteRepository extends PagingAndSortingRepository<UserFavoriteEntity, Long> {

    @Query("select count(*) from UserFavoriteEntity where courseId = :courseId")
    int getCourseFavoriteCount(@Param("courseId") int courseId);

    List<UserFavoriteEntity> findByUserIdAndCourseId(int userId, int courseId);

    @Query("select c from CourseEntity as c, UserFavoriteEntity as u where u.userId = :userId and u.courseId = c.id order by u.timeCreated desc")
    List<CourseEntity> findCoursesByUserId(@Param("userId") int userId);

    List<Long> deleteByUserIdAndCourseId(int userId, int courseId);

    List<Long> deleteByCourseId(int courseId);
}
