package com.education.db.jpa;

import com.education.db.entity.CourseTagRelationEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Created by yzzhao on 12/23/15.
 */
public interface CourseTagRelationRepository extends CrudRepository<CourseTagRelationEntity, Integer>{

    List<CourseTagRelationEntity> findCourseTagsByCourseId(int courseId);

    void removeByCourseId(int courseId);

    void removeByCourseTagId(int courseTagId);

    List<CourseTagRelationEntity> findCoursesTagsByCourseTagId(int courseTagId);

}
