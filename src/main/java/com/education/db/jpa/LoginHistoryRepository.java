package com.education.db.jpa;

import com.education.db.entity.LoginHistoryEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Created by yzzhao on 12/21/15.
 */
public interface LoginHistoryRepository extends CrudRepository<LoginHistoryEntity, Integer> {

    List<LoginHistoryEntity> findByUseridOrderByLoginTimeDesc(int userId);

    int countByuserid(int userId);

    List<LoginHistoryEntity> findByUseridOrderByLoginTimeAsc(int userId);
}
