package com.education.db.jpa;

import com.education.db.entity.CourseTagEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Created by yzzhao on 12/24/15.
 */
public interface CourseTagRepository extends CrudRepository<CourseTagEntity, Integer>{

    List<CourseTagEntity> findCourseTagByName(String name);
}
