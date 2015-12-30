package com.education.service;

import com.education.db.entity.CommonStatus;
import com.education.db.entity.CourseEntity;
import com.education.db.entity.UserEntity;
import com.education.db.jpa.CourseRepository;
import com.education.db.jpa.UserRepository;
import com.education.ws.CourseRegisterBean;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
    public void testCourseProposalQuery1(){
        List<CourseRegisterBean> beans = courseProposalService.queryCourse(null, 1, CommonStatus.ENABLED.name());
        long size = beans.size();
        CourseEntity course =new CourseEntity();
        course.setStatus(CommonStatus.ENABLED);
        course.setName("course1");
        course.setCategory(1);
        courseRepository.save(course);
        beans = courseProposalService.queryCourse(null, 1, CommonStatus.ENABLED.name());
        Assert.assertEquals(size+1, beans.size());
    }
}
