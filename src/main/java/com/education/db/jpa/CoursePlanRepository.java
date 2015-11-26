package com.education.db.jpa;

import com.education.db.entity.CoursePlanEntity;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by yzzhao on 11/25/15.
 */
public interface CoursePlanRepository extends CrudRepository<CoursePlanEntity, Integer> {
}
