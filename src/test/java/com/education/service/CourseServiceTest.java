package com.education.service;

import com.education.db.entity.CommonStatus;
import com.education.db.entity.CourseTagRelationEntity;
import com.education.db.jpa.CourseRepository;
import com.education.db.jpa.CourseTagRelationRepository;
import com.education.db.jpa.CourseTypeRepository;
import com.education.formbean.CourseQueryBean;
import com.education.ws.CourseRegisterBean;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by yzzhao on 12/5/15.
 */
@Transactional
public class CourseServiceTest extends AbstractServiceTest {

    @Autowired
    private CourseService courseService;

    @Autowired
    private CourseTypeRepository courseTypeRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private CourseTagRelationRepository courseTagRelationRepository;

    @Test
    public void testCreateCourse() {
        String name = System.currentTimeMillis() + "";
        CourseRegisterBean bean = createCourseBean(name);
        courseService.createCourse(bean);
        List<CourseQueryBean> allCoursesIndex = courseService.getAllCoursesIndex();
        boolean found = false;
        for (CourseQueryBean b : allCoursesIndex) {
            if (b.getName().equals(name)) {
                found = true;
                break;
            }
        }
        Assert.assertTrue(found);
    }

    @Test
    public void testCreateCourseVideoUrl() {
        String name = System.currentTimeMillis() + "";
        CourseRegisterBean courseBean = createCourseBean(name);
        courseBean.setVideoExternalUrl("xxx");
        int courseId = courseService.createCourse(courseBean);
        CourseRegisterBean queryCourse = courseService.queryCourse(courseId + "");
        Assert.assertNotNull(queryCourse);
        Assert.assertEquals("xxx", queryCourse.getVideoExternalUrl());
    }

    @Test
    public void testCreateCourseYear() {
        String name = System.currentTimeMillis() + "";
        CourseRegisterBean courseBean = createCourseBean(name);
        courseBean.setYears(3);
        int courseId = courseService.createCourse(courseBean);

        CourseRegisterBean queryCourse = courseService.queryCourse(courseId + "");
        Assert.assertNotNull(queryCourse);
        Assert.assertEquals(3, queryCourse.getYears());
    }

    @Test
    public void testCreateCourseStatus() {
        String name = System.currentTimeMillis() + "";
        CourseRegisterBean courseBean = createCourseBean(name);
        courseBean.setStatus(CommonStatus.DISABLED.name());
        int courseId = courseService.createCourse(courseBean);

        CourseRegisterBean queryCourse = courseService.queryCourse(courseId + "");
        Assert.assertNotNull(queryCourse);
        Assert.assertEquals(CommonStatus.DISABLED.name(), queryCourse.getStatus());
    }

    @Test
    public void testCreateCourseWithTags() {
        String name = System.currentTimeMillis() + "";
        CourseRegisterBean courseBean = createCourseBean(name);
        courseBean.setTags("3,2,4,5");
        int courseId = courseService.createCourse(courseBean);
        List<CourseTagRelationEntity> tags = courseTagRelationRepository.findCourseTagsByCourseId(courseId);
        Assert.assertEquals(4, tags.size());
        Assert.assertEquals(3, tags.get(0).getCourseTagId());
        Assert.assertEquals(2, tags.get(1).getCourseTagId());
        Assert.assertEquals(4, tags.get(2).getCourseTagId());
        Assert.assertEquals(5, tags.get(3).getCourseTagId());
        List<CourseQueryBean> allCoursesIndex = courseService.getAllCoursesIndex();
        Assert.assertTrue(!allCoursesIndex.isEmpty());
    }

    private CourseRegisterBean createCourseBean(String name) {
        int courseTypeId = courseTypeRepository.findAll().iterator().next().getId();
        CourseRegisterBean bean = new CourseRegisterBean();
        bean.setCategory(String.valueOf(courseTypeId));
        bean.setName(name);
        bean.setTitleImagePath("aaa");
        bean.setYears(13);
        bean.setStatus(CommonStatus.ENABLED.name());
        bean.setContent("test");
        return bean;
    }


}
