package com.education.db.jpa;
import com.education.db.entity.CourseEntity;
import com.education.ws.CourseRegisterBean;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Date;
import java.util.List;

/**
 * Created by yzzhao on 11/11/15.
 */
public interface CourseRepository extends CrudRepository<CourseEntity, Integer>{

    @Query("select name from CourseEntity")
    List<String> findAllCourseNames();

    List<CourseEntity> findByDateAfter(Date date);

    List<CourseEntity> findByCategory(String category);

    List<CourseEntity> findByCategoryAndDateAfter(String category, Date date);

    List<CourseEntity> findByCategoryAndDateBefore(String category, Date date);

    List<CourseEntity> findByCategoryAndDate(String category, Date date);

}
