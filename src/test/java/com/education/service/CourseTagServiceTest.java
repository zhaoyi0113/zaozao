package com.education.service;

import com.education.db.entity.CourseTagEntity;
import com.education.db.entity.CourseTagRelationEntity;
import com.education.db.jpa.CourseTagRelationRepository;
import com.education.db.jpa.CourseTagRepository;
import com.education.exception.BadRequestException;
import com.education.formbean.CourseTagBean;
import com.education.ws.CourseRegisterBean;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by yzzhao on 12/24/15.
 */
@Transactional
public class CourseTagServiceTest extends AbstractServiceTest {

    @Autowired
    private CourseTagService courseTagService;

    @Autowired
    private CourseTagRepository courseTagRepository;

    @Autowired
    private CourseService courseService;

    @Autowired
    private CourseTagRelationRepository courseTagRelationRepository;

    @Test
    public void testCreateCourseTag() {
        String name = String.valueOf(System.currentTimeMillis());
        int tagId = courseTagService.addNewCourseTag(name, name, null);
        CourseTagEntity courseTag = courseTagRepository.findOne(tagId);
        Assert.assertNotNull(courseTag);
        Assert.assertEquals(name, courseTag.getName());
    }

    @Test
    public void testDeleteCourseTag() {
        String name = String.valueOf(System.currentTimeMillis());
        CourseRegisterBean courseBean = CourseServiceTest.createCourseBean(name);
        int courseTagId = courseTagService.addNewCourseTag("1", "1", null);
        courseBean.setTags(courseTagId + ",2,3");
        int courseId = courseService.createCourse(courseBean);
        courseTagService.deleteCourseTag(courseTagId);
        BadRequestException ex = null;
        try {
            CourseTagBean courseTag = courseTagService.getCourseTag(courseTagId);
        } catch (BadRequestException e) {
            ex = e;
        }
        Assert.assertNotNull(ex);
        List<CourseTagRelationEntity> courseTagRels = courseTagRelationRepository.findCoursesTagsByCourseTagId(courseTagId);
        Assert.assertEquals(0, courseTagRels.size());
        List<CourseTagRelationEntity> courseTagsByCourseId = courseTagRelationRepository.findCourseTagsByCourseId(courseId);
        Assert.assertEquals(2, courseTagsByCourseId.size());
    }

    @Test
    public void testEditCourseTag(){
        String name = String.valueOf(System.currentTimeMillis());
        int tagId = courseTagService.addNewCourseTag(name, name, null);
        courseTagService.editCourseTag(tagId, "aaa",null,null);
        CourseTagBean courseTag = courseTagService.getCourseTag(tagId);
        Assert.assertNotNull(courseTag);
        Assert.assertEquals("aaa", courseTag.getName());
    }

    @Test
    public void testGetCourseTag(){
        String name = String.valueOf(System.currentTimeMillis());
        CourseRegisterBean courseBean = CourseServiceTest.createCourseBean(name);
        int courseTagId = courseTagService.addNewCourseTag("1", "1", null);
        courseBean.setTags(courseTagId + ",2,3");
        int courseId = courseService.createCourse(courseBean);
        List<CourseTagBean> tags = courseTagService.getCourseTagsByCourseId(courseId);
        Assert.assertEquals(3, tags.size());
    }
}
