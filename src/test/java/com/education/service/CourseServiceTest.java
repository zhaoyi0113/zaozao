package com.education.service;

import com.education.db.entity.CommonStatus;
import com.education.db.entity.CourseEntity;
import com.education.db.entity.CourseTagRelationEntity;
import com.education.db.jpa.CourseRepository;
import com.education.db.jpa.CourseTagRelationRepository;
import com.education.db.jpa.CourseTypeRepository;
import com.education.formbean.CourseQueryBean;
import com.education.ws.CourseRegisterBean;
import com.education.ws.util.WSUtility;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.Date;
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
        CourseQueryBean queryCourse = courseService.queryCourse(courseId + "");
        Assert.assertNotNull(queryCourse);
        Assert.assertEquals("xxx", queryCourse.getVideoUrl());
    }

    @Test
    public void testCreateCourseStatus() {
        String name = System.currentTimeMillis() + "";
        CourseRegisterBean courseBean = createCourseBean(name);
        courseBean.setStatus(CommonStatus.DISABLED.name());
        int courseId = courseService.createCourse(courseBean);

        CourseQueryBean queryCourse = courseService.queryCourse(courseId + "");
        Assert.assertNotNull(queryCourse);
        Assert.assertEquals(CommonStatus.DISABLED, queryCourse.getStatus());
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
    }

    @Test
    public void testCreatCoursePublishDate() {
        String name = System.currentTimeMillis() + "";
        CourseRegisterBean courseBean = createCourseBean(name);
        Date date = Calendar.getInstance().getTime();
        courseBean.setPublishDate(WSUtility.dateToString(date));
        int courseId = courseService.createCourse(courseBean);
        CourseQueryBean course = courseService.queryCourse(String.valueOf(courseId));
        Assert.assertEquals(WSUtility.dateToString(date), course.getPublishDate());
    }

    protected static CourseRegisterBean createCourseBean(String name) {
        CourseRegisterBean bean = new CourseRegisterBean();
        bean.setName(name);
        bean.setTitleImagePath("aaa");
        bean.setYears(13);
        bean.setStatus(CommonStatus.ENABLED.name());
        bean.setContent("test");
        return bean;
    }

    @Test
    public void editCourseTest() {
        String name = System.currentTimeMillis() + "";
        CourseRegisterBean courseBean = createCourseBean(name);
        courseBean.setTags("1,2,3");
        int courseId = courseService.createCourse(courseBean);
        courseBean.setId(String.valueOf(courseId));
        courseBean.setTags("4,1");
        courseService.editCourse(courseBean);
        List<CourseTagRelationEntity> tagRels = courseTagRelationRepository.findCourseTagsByCourseId(Integer.parseInt(courseBean.getId()));
        Assert.assertEquals(2, tagRels.size());
        Assert.assertEquals(4, tagRels.get(0).getCourseTagId());
        Assert.assertEquals(1, tagRels.get(1).getCourseTagId());
    }

    @Test
    public void deleteCourse() {
        String name = System.currentTimeMillis() + "";
        CourseRegisterBean courseBean = createCourseBean(name);
        courseBean.setTags("1,2,3");
        int courseId = courseService.createCourse(courseBean);
        List<CourseTagRelationEntity> tags = courseTagRelationRepository.findCourseTagsByCourseId(courseId);
        Assert.assertEquals(3, tags.size());
        courseService.deleteCourse(courseId);
        CourseEntity course = courseRepository.findOne(courseId);
        Assert.assertNull(course);
        tags = courseTagRelationRepository.findCourseTagsByCourseId(courseId);
        Assert.assertEquals(0, tags.size());
    }
}
