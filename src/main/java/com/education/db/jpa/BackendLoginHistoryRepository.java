package com.education.db.jpa;

import com.education.db.entity.BackendLoginHistoryEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Created by yzzhao on 12/26/15.
 */
public interface BackendLoginHistoryRepository extends CrudRepository<BackendLoginHistoryEntity,Integer>{

    List<BackendLoginHistoryEntity> findByUserId(int userId);
}
