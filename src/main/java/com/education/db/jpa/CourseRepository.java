package com.education.db.jpa;

import com.education.db.entity.CommonStatus;
import com.education.db.entity.CourseEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import javax.ws.rs.QueryParam;
import java.util.Date;
import java.util.List;

/**
 * Created by yzzhao on 11/11/15.
 */
public interface CourseRepository extends PagingAndSortingRepository<CourseEntity, Integer> {

    @Query("select name from CourseEntity")
    List<String> findAllCourseNames();

    List<CourseEntity> findByName(String name);

    List<CourseEntity> findByDateAfter(Date date);

    List<CourseEntity> findByCategoryAndDateAfter(String category, Date date);

    List<CourseEntity> findByCategoryAndDateBefore(String category, Date date);

    List<CourseEntity> findByCategoryAndDate(String category, Date date);

    List<CourseEntity> findAllByOrderByTimeCreatedDesc();

    @Query("select c from CourseEntity c where status= :status and publishDate<= :date order by publishDate desc, c.id desc")
    List<CourseEntity> findEnabledCoursesByStatus(@Param("status") CommonStatus status, @Param("date") Date date);

    @Query("select c from CourseEntity c, CourseTagRelationEntity ctr where c.id = ctr.courseId and ctr.courseTagId = :courseTagId and c.status = :status and c.publishDate <= :date order by c.publishDate desc, c.id desc")
    List<CourseEntity> findEnabledCoursesByStatusAndCourseTag(@Param("status") CommonStatus status, @Param("courseTagId") int courseTagId, @Param("date") Date date);

    //select name, count(name) from user_course_history as u, course as c where u.course_id = c.id group by name;

    @Query("select c.id, count(c.id) from CourseEntity c, UserCourseHistoryEntity u where c.id = u.courseId group by c")
    List<Object[]> findCourseViewCount();

    @Query("select c.id, count(c.id) from CourseEntity c, UserCourseHistoryEntity u where c.id = u.courseId and u.userId= :userId group by c")
    List<Object[]> findCourseViewCountByUser(@Param("userId") int userId);

    @Query("select count(*) from CourseEntity c, CourseTagRelationEntity ctr where ctr.courseTagId = :tagId and ctr.courseId = c.id ")
    public long getCourseCountByTag(@Param("tagId") int tagId);

    @Query("select id, name from CourseEntity")
    List<Object[]> findIdAndNames();

    @Query("select sum(pv) from CourseEntity")
    long getTotalPv();
}
