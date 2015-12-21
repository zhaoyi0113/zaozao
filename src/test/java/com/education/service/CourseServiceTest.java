package com.education.service;

import com.education.db.entity.CourseEntity;
import com.education.db.jpa.CourseRepository;
import com.education.db.jpa.CourseTypeRepository;
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

    @Test
    public void testCreateCourse() {
        String name = System.currentTimeMillis() + "";
        CourseRegisterBean bean = createCourseBean(name);
        courseService.createCourse(bean);
        List<CourseRegisterBean> allCoursesIndex = courseService.getAllCoursesIndex();
        bean = null;
        for (CourseRegisterBean b : allCoursesIndex) {
            if (b.getName().equals(name)) {
                bean = b;
                break;
            }
        }
        Assert.assertNotNull(bean);
        name = System.currentTimeMillis() + "";
        bean.setName(name);
    }

    @Test
    public void testCreateCourseVideoUrl() {
        String name = System.currentTimeMillis() + "";
        CourseRegisterBean courseBean = createCourseBean(name);
        courseBean.setVideoExternalUrl("xxx");
        int courseId = courseService.createCourse(courseBean);
        CourseRegisterBean queryCourse = courseService.queryCourse(courseId+"");
        Assert.assertNotNull(queryCourse);
        Assert.assertEquals("xxx", queryCourse.getVideoExternalUrl());
    }

    @Test
    public void testCreateCourseYear() {
        String name = System.currentTimeMillis() + "";
        CourseRegisterBean courseBean = createCourseBean(name);
        courseBean.setYears(3);
        int courseId = courseService.createCourse(courseBean);

        CourseRegisterBean queryCourse = courseService.queryCourse(courseId+"");
        Assert.assertNotNull(queryCourse);
        Assert.assertEquals(3, queryCourse.getYears());
    }

    private CourseRegisterBean createCourseBean(String name) {
        int courseTypeId = courseTypeRepository.findAll().iterator().next().getId();
        CourseRegisterBean bean = new CourseRegisterBean();
        bean.setCategory(String.valueOf(courseTypeId));
        bean.setName(name);
        bean.setTitleImagePath("aaa");
        bean.setYears(13);
        bean.setContent("test");
        return bean;
    }


}
