package com.education.service;

import com.education.db.entity.CommonStatus;
import com.education.db.entity.CourseEntity;
import com.education.db.entity.CourseTagRelationEntity;
import com.education.db.entity.UserEntity;
import com.education.db.jpa.CourseRepository;
import com.education.db.jpa.CourseTagRelationRepository;
import com.education.db.jpa.UserRepository;
import com.education.ws.CourseRegisterBean;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by yzzhao on 12/21/15.
 */
@Transactional
public class CourseProposalServiceTest extends AbstractServiceTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CourseProposalService courseProposalService;

    @Autowired
    private LoginHistoryService loginHistoryService;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private CourseTagRelationRepository courseTagRelationRepository;

    @Test
    @Ignore
    public void testCourseProposalEmpty() {
        UserEntity user = new UserEntity();
        user.setOpenid("1");
        user.setUserName("aaa");

        UserEntity saved = userRepository.save(user);

        WeChatUserInfo userInfo = new WeChatUserInfo();
        userInfo.setOpenid(saved.getOpenid());

//        List<CourseRegisterBean> course = courseProposalService.proposeCourse(userInfo);
//        Assert.assertEquals(0, course.size());

    }

    @Test
    public void testCourseProposalQuery1() {
        List<CourseRegisterBean> beans = courseProposalService.queryCourse(null, 0, CommonStatus.ENABLED.name(), 0);
        long size = beans.size();
        CourseEntity course = new CourseEntity();
        course.setStatus(CommonStatus.ENABLED);
        course.setName("course1");
        course.setCategory(1);
        course.setPublishDate(Calendar.getInstance().getTime());
        courseRepository.save(course);
        beans = courseProposalService.queryCourse(null, 0, CommonStatus.ENABLED.name(), 0);
        Assert.assertEquals(size + 1, beans.size());
    }

    @Test
    public void testCourseTagQuery1() {
        CourseEntity course = new CourseEntity();
        course.setStatus(CommonStatus.ENABLED);
        course.setName("course1");
        course.setPublishDate(Calendar.getInstance().getTime());
        CourseEntity saved = courseRepository.save(course);
        CourseTagRelationEntity ctrEntity = new CourseTagRelationEntity();
        ctrEntity.setCourseId(saved.getId());
        ctrEntity.setCourseTagId(99);
        courseTagRelationRepository.save(ctrEntity);
        List<CourseRegisterBean> beans = courseProposalService.queryCourse(null, 99, CommonStatus.ENABLED.name(), 0);
        Assert.assertEquals(1, beans.size());
    }

    @Test
    public void testQueryNumberOfCourses() {
        int length = 10;
        for (int i = 0; i < length; i++) {
            CourseEntity course = new CourseEntity();
            course.setStatus(CommonStatus.ENABLED);
            course.setName("course1");
            course.setPublishDate(Calendar.getInstance().getTime());
            CourseEntity saved = courseRepository.save(course);
            CourseTagRelationEntity ctrEntity = new CourseTagRelationEntity();
            ctrEntity.setCourseId(saved.getId());
            ctrEntity.setCourseTagId(99);
            courseTagRelationRepository.save(ctrEntity);
        }
        List<CourseRegisterBean> beans = courseProposalService.queryCourse(null, 99, CommonStatus.ENABLED.name(), 3);
        Assert.assertEquals(3, beans.size());
    }

    @Test
    public void testQueryCouseByOrder() {
        List<Integer> lastThree = new ArrayList<>();
        int length = 10;
        Calendar instance = Calendar.getInstance();
        for (int i = 0; i < length; i++) {
            CourseEntity course = new CourseEntity();
            course.setStatus(CommonStatus.ENABLED);
            course.setName("course1");
            course.setPublishDate(instance.getTime());
            CourseEntity saved = courseRepository.save(course);
            CourseTagRelationEntity ctrEntity = new CourseTagRelationEntity();
            ctrEntity.setCourseId(saved.getId());
            ctrEntity.setCourseTagId(99);
            courseTagRelationRepository.save(ctrEntity);
            instance.add(Calendar.DAY_OF_YEAR,-1);
            if (i <3) {
                lastThree.add(saved.getId());
            }
        }
        List<CourseRegisterBean> beans = courseProposalService.queryCourse(null, 99, CommonStatus.ENABLED.name(), 3);
        Assert.assertEquals(3, beans.size());
        for (int i = 0; i < lastThree.size(); i++) {
            Assert.assertEquals(lastThree.get(i)+"", beans.get(i).getId());
        }
    }

    @Test
    public void testQueryMapByDate(){
        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
        try {
            Date date = format.parse("2015/12/1");
            CourseEntity course = new CourseEntity();
            course.setStatus(CommonStatus.ENABLED);
            course.setName("course1");
            course.setPublishDate(date);
            courseRepository.save(course);

            date = format.parse("2015/12/2");
            course = new CourseEntity();
            course.setStatus(CommonStatus.ENABLED);
            course.setName("course2");
            course.setPublishDate(date);
            courseRepository.save(course);

            date = format.parse("2015/12/2");
            course = new CourseEntity();
            course.setStatus(CommonStatus.ENABLED);
            course.setName("course3");
            course.setPublishDate(date);
            courseRepository.save(course);


            date = format.parse("2015/12/3");
            course = new CourseEntity();
            course.setStatus(CommonStatus.ENABLED);
            course.setName("course4");
            course.setPublishDate(date);
            courseRepository.save(course);

            Map<Date, List<CourseRegisterBean>> courseMap = courseProposalService.queryCourseByDate(null, 0, CommonStatus.ENABLED.name(), 10);
            Assert.assertTrue(courseMap.size()>=3);

        } catch (ParseException e) {
            e.printStackTrace();
            Assert.fail(e.getMessage());
        }
    }
}
