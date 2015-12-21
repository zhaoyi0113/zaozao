package com.education.db.jpa;

import com.education.db.entity.CourseTypeEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Created by yzzhao on 12/2/15.
 */
public interface CourseTypeRepository extends CrudRepository<CourseTypeEntity, Integer>{

    CourseTypeEntity findByName(String name);
}
