package com.education.db.jpa;

import com.education.db.entity.BackendUserTrackingEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Created by yzzhao on 1/26/16.
 */
public interface BackendUserTrackingRepository extends CrudRepository<BackendUserTrackingEntity, Long> {

    List<BackendUserTrackingEntity> findByUserId(int userId);
}
