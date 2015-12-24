package com.education.service;

import com.education.db.entity.CourseTagEntity;
import com.education.db.jpa.CourseTagRepository;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by yzzhao on 12/24/15.
 */
@Transactional
public class CourseTagServiceTest extends AbstractServiceTest {

    @Autowired
    private CourseTagService courseTagService;

    @Autowired
    private CourseTagRepository courseTagRepository;

    @Test
    public void testCreateCourseTag(){
        String name = String.valueOf(System.currentTimeMillis());
        int tagId = courseTagService.addNewCourseTag(name);
        CourseTagEntity courseTag = courseTagRepository.findOne(tagId);
        Assert.assertNotNull(courseTag);
        Assert.assertEquals(name, courseTag.getName());
    }
}
