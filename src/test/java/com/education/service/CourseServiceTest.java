package com.education.service;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * Created by yzzhao on 12/5/15.
 */
@Transactional
public class CourseServiceTest extends AbstractServiceTest{

    @Autowired
    private CourseService courseService;

    @Test
    public void testQueryCourseByCategory(){
        List maps = courseService.queryCourseByCategory("");
        Assert.assertEquals(0, maps.size());
    }

}
