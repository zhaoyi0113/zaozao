package com.education.db.jpa;

import com.education.db.entity.CourseEntity;
import com.education.db.entity.HomeCourseEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Created by yzzhao on 1/20/16.
 */
public interface HomeCourseRepository extends CrudRepository<HomeCourseEntity, Integer> {

    HomeCourseEntity findTopByOrderByOrderIndexDesc();

    List<HomeCourseEntity> findByCourseId(int courseId);

    List<HomeCourseEntity> findByOrderIndexLessThanOrderByOrderIndex(int index);

    List<HomeCourseEntity> findByOrderIndexGreaterThanOrderByOrderIndex(int orderIndex);

    @Query("select h from HomeCourseEntity h order by order_index")
    List<HomeCourseEntity> findOrderByOrderIndex();

    @Query("select c from CourseEntity as c, HomeCourseEntity as h where h.courseId = c.id order by order_index")
    List<CourseEntity> findAllCourses();
}
