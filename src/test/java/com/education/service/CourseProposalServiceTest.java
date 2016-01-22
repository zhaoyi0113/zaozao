package com.education.service;

import com.education.db.entity.CommonStatus;
import com.education.db.entity.CourseEntity;
import com.education.db.entity.CourseTagRelationEntity;
import com.education.db.entity.UserEntity;
import com.education.db.jpa.CourseRepository;
import com.education.db.jpa.CourseTagRelationRepository;
import com.education.db.jpa.UserRepository;
import com.education.formbean.CourseQueryBean;
import com.github.springtestdbunit.annotation.DatabaseSetup;
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
        List<CourseQueryBean> beans = courseProposalService.queryCourses(null, 0, CommonStatus.ENABLED.name(), 0, 0, 0);
        int size = beans.size();
        CourseEntity course = new CourseEntity();
        course.setStatus(CommonStatus.ENABLED);
        course.setName("course1");
        course.setCategory(1);
        course.setPublishDate(Calendar.getInstance().getTime());
        CourseEntity save = courseRepository.save(course);
        beans = courseProposalService.queryCourses(null, 0, CommonStatus.ENABLED.name(), 0, 0, 0);
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
        List<CourseQueryBean> beans = courseProposalService.queryCourses(null, 99, CommonStatus.ENABLED.name(), 0, 0, 0);
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
        List<CourseQueryBean> beans = courseProposalService.queryCourses(null, 99, CommonStatus.ENABLED.name(), 3, 0, 0);
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
            instance.add(Calendar.DAY_OF_YEAR, -1);
            if (i < 3) {
                lastThree.add(saved.getId());
            }
        }
        List<CourseQueryBean> beans = courseProposalService.queryCourses(null, 99, CommonStatus.ENABLED.name(), 3, 0, 0);
        Assert.assertEquals(3, beans.size());
        for (int i = 0; i < lastThree.size(); i++) {
            Assert.assertEquals(lastThree.get(i) + "", beans.get(i).getId());
        }
    }

    @Test
    public void testQueryMapByDate() {
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

            Map<String, List<CourseQueryBean>> courseMap = courseProposalService.queryCourseByDate(null, 0, CommonStatus.ENABLED.name(), 10, 0);
            int num = 0;
            for (Map.Entry<String, List<CourseQueryBean>> entry : courseMap.entrySet()) {
                num += entry.getValue().size();
            }
            Assert.assertTrue(num >= 3);
        } catch (ParseException e) {
            e.printStackTrace();
            Assert.fail(e.getMessage());
        }
    }

    @Test
    public void testQueryWithIgnore() {
        CourseEntity course = new CourseEntity();
        course.setStatus(CommonStatus.ENABLED);
        course.setName("course1");
        course.setPublishDate(Calendar.getInstance().getTime());
        course = courseRepository.save(course);
        List<CourseQueryBean> courseList = courseProposalService.queryCourses(null, 0, CommonStatus.ENABLED.name(), 10, 0, 0);
        boolean found = false;
        for (CourseQueryBean bean : courseList) {
            if (bean.getId().equals(String.valueOf(course.getId()))) {
                found = true;
                break;
            }
        }
        Assert.assertTrue(found);
        courseList = courseProposalService.queryCourses(null, 0, CommonStatus.ENABLED.name(), 10, 0, course.getId());
        found = false;
        for (CourseQueryBean bean : courseList) {
            if (bean.getId().equals(String.valueOf(course.getId()))) {
                found = true;
                break;
            }
        }
        Assert.assertFalse(found);
    }

    @Test
    @DatabaseSetup(value = "classpath:/com/education/service/course_proposal_service_test.xml")
    public void testQueryHomeCourses() {
        List<CourseQueryBean> courseQueryBeans = courseProposalService.queryHomeCourses();
        Assert.assertEquals(3, courseQueryBeans.size());
        Assert.assertEquals(2000 + "", courseQueryBeans.get(0).getId());
        Assert.assertEquals(2001 + "", courseQueryBeans.get(1).getId());
        Assert.assertEquals(2002 + "", courseQueryBeans.get(2).getId());
    }
}
