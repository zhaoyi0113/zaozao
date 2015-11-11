package com.education.db.jpa;
import com.education.db.entity.CourseEntity;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by yzzhao on 11/11/15.
 */
public interface CourseRepository extends CrudRepository<CourseEntity, Integer>{


}
