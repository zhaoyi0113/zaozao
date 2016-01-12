package com.education.service;

import com.github.springtestdbunit.annotation.DatabaseSetup;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

/**
 * Created by yzzhao on 1/12/16.
 */
@Transactional
public class CourseAnalyseServiceTest extends AbstractServiceTest {

    @Autowired
    private CourseAnalyseService analyseService;

    @Test
    @DatabaseSetup(value="classpath:/com/education/service/course_analyse_service_test.xml")
    public void testCourseClick(){
        Map<Integer, Long> query = analyseService.getCoursePreviewCount();
        Assert.assertEquals((Long)3l, query.get(10000));
        Assert.assertEquals((Long)2l, query.get(10001));
        Assert.assertNull(query.get(10003));
    }

    @Test
    @DatabaseSetup(value="classpath:/com/education/service/course_analyse_service_test.xml")
    public void testCourseClickByUser(){
        Map<Integer, Long> query = analyseService.getCoursePreviewCountByUser(30000);
        Assert.assertEquals(1, query.size());
        Assert.assertEquals((Long)1l, query.get(10000));
        Assert.assertNull(query.get(10002));
        Assert.assertNull(query.get(10003));

        query = analyseService.getCoursePreviewCountByUser(30001);
        Assert.assertEquals(2, query.size());
        Assert.assertEquals((Long)2l, query.get(10000));
        Assert.assertEquals((Long)1l, query.get(10001));

        query = analyseService.getCoursePreviewCountByUser(30002);
        Assert.assertEquals(1, query.size());
        Assert.assertEquals((Long)1l, query.get(10001));
    }


}
