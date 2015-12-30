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

//    @QueryParam("select * from CourseEntity c where c.category = :category_id and c.status  = :status")
//    List<CourseEntity> findEnabledCoursesByCategoryIdAndStatus(@Param("category_id") int category,@Param("status") CommonStatus status);

    @Query("select c from CourseEntity c where status= :status and category_id = :category_id")
    List<CourseEntity> findEnabledCoursesByStatusAndCategory(@Param("status") CommonStatus status, @Param("category_id") int categoryId);
}
