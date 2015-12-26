package com.education.db.jpa;

import com.education.db.entity.HomeConfigEntity;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by yzzhao on 12/25/15.
 */
public interface HomeConfigRepository extends CrudRepository<HomeConfigEntity, Integer> {
}
