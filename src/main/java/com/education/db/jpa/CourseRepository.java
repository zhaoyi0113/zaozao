package com.education.db.jpa;
import com.education.db.entity.CommonStatus;
import com.education.db.entity.CourseEntity;
import com.education.ws.CourseRegisterBean;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import javax.ws.rs.QueryParam;
import java.util.Date;
import java.util.List;

/**
 * Created by yzzhao on 11/11/15.
 */
public interface CourseRepository extends CrudRepository<CourseEntity, Integer>{

    @Query("select name from CourseEntity")
    List<String> findAllCourseNames();

    List<CourseEntity> findByName(String name);

    List<CourseEntity> findByDateAfter(Date date);

    List<CourseEntity> findByCategoryAndDateAfter(String category, Date date);

    List<CourseEntity> findByCategoryAndDateBefore(String category, Date date);

    List<CourseEntity> findByCategoryAndDate(String category, Date date);

    @Query("select c from CourseEntity c where status= :status and publishDate<= :date order by publishDate desc")
    List<CourseEntity> findEnabledCoursesByStatus(@Param("status") CommonStatus status, @Param("date") Date date);

    @Query("select c from CourseEntity c, CourseTagRelationEntity ctr where c.id = ctr.courseId and ctr.courseTagId = :courseTagId and c.status = :status and c.publishDate <= :date order by c.publishDate desc")
    List<CourseEntity> findEnabledCoursesByStatusAndCourseTag(@Param("status") CommonStatus status, @Param("courseTagId") int courseTagId, @Param("date") Date date);

}
