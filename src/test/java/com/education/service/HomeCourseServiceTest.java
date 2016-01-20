package com.education.service;

import com.education.exception.BadRequestException;
import com.education.formbean.HomeCourseBean;
import com.education.util.MoveAction;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by yzzhao on 1/20/16.
 */
@Transactional
public class HomeCourseServiceTest extends AbstractServiceTest {

    @Autowired
    private HomeCourseService homeCourseService;

    @Test
    @DatabaseSetup(value = "classpath:/com/education/service/homecourse_service_test.xml")
    public void testGetHomeCourseList() {
        List<HomeCourseBean> homeCourses = homeCourseService.getHomeCourses();
        Assert.assertEquals(4, homeCourses.size());

    }

    @Test
    @DatabaseSetup(value = "classpath:/com/education/service/homecourse_service_test.xml")
    public void testAddNewCourse() {
        int size = homeCourseService.getHomeCourses().size();
        homeCourseService.addCourseOnHomePage(2001);
        Assert.assertEquals(size + 1, homeCourseService.getHomeCourses().size());
    }

    @Test
    @DatabaseSetup(value = "classpath:/com/education/service/homecourse_service_test.xml")
    public void testAddDuplicateCourse() {
        int size = homeCourseService.getHomeCourses().size();
        homeCourseService.addCourseOnHomePage(2001);
        BadRequestException ex = null;
        try {
            homeCourseService.addCourseOnHomePage(2001);
        } catch (BadRequestException e) {
            ex = e;
        }
        Assert.assertNotNull(ex);
    }

    @Test
    @DatabaseSetup(value = "classpath:/com/education/service/homecourse_service_test.xml")
    public void testMoveUp() {
        homeCourseService.moveAction(10000, MoveAction.UP.name());
        List<HomeCourseBean> homeCourses = homeCourseService.getHomeCourses();
        testCourseId(new int[]{10000, 10001, 10002, 10003}, homeCourses);
        homeCourseService.moveAction(10001, MoveAction.UP.name());
        homeCourses = homeCourseService.getHomeCourses();
        testCourseId(new int[]{10001, 10000, 10002, 10003}, homeCourses);
        homeCourseService.moveAction(10002, MoveAction.UP.name());
        homeCourses = homeCourseService.getHomeCourses();
        testCourseId(new int[]{10001, 10002, 10000, 10003}, homeCourses);
        homeCourseService.moveAction(10003, MoveAction.UP.name());
        homeCourses = homeCourseService.getHomeCourses();
        testCourseId(new int[]{10001, 10002, 10003, 10000}, homeCourses);
    }


    @Test
    @DatabaseSetup(value = "classpath:/com/education/service/homecourse_service_test.xml")
    public void testMoveDown() {
        homeCourseService.moveAction(10000, MoveAction.DOWN.name());
        List<HomeCourseBean> homeCourses = homeCourseService.getHomeCourses();
        testCourseId(new int[]{10001, 10000, 10002, 10003}, homeCourses);
        homeCourseService.moveAction(10001, MoveAction.DOWN.name());
        homeCourses = homeCourseService.getHomeCourses();
        testCourseId(new int[]{10000, 10001, 10002, 10003}, homeCourses);
        homeCourseService.moveAction(10002, MoveAction.DOWN.name());
        homeCourses = homeCourseService.getHomeCourses();
        testCourseId(new int[]{10000, 10001, 10003, 10002}, homeCourses);
        homeCourseService.moveAction(10002, MoveAction.DOWN.name());
        homeCourses = homeCourseService.getHomeCourses();
        testCourseId(new int[]{10000, 10001, 10003, 10002}, homeCourses);
    }

    private static void testCourseId(int[] courseIds, List<HomeCourseBean> courses) {
        Assert.assertEquals(courseIds.length, courses.size());
        for (int i = 0; i < courseIds.length; i++) {
            Assert.assertEquals(courseIds[i], courses.get(i).getId());
        }
    }
}
