package com.education.db.jpa;

import com.education.db.entity.ChildrenEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Created by yzzhao on 12/17/15.
 */
public interface ChildrenRepository extends CrudRepository<ChildrenEntity, Integer> {
    List<ChildrenEntity> findByParentId(int parentId);

    void deleteByParentId(int parentId);
}
