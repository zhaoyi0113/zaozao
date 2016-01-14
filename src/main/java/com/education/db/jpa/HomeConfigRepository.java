package com.education.db.jpa;

import com.education.db.entity.HomeConfigEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Created by yzzhao on 12/25/15.
 */
public interface HomeConfigRepository extends CrudRepository<HomeConfigEntity, Integer> {

    @Query("select h from HomeConfigEntity h order by order_index")
    List<HomeConfigEntity> findOrderByOrderIndex();

    List<HomeConfigEntity> findByOrderIndexGreaterThan(int index);

    List<HomeConfigEntity> findByOrderIndexLessThan(int index);
}
