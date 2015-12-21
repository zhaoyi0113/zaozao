package com.education.service;

import com.education.db.entity.UserEntity;
import com.education.db.jpa.UserRepository;
import com.education.ws.CourseRegisterBean;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

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

    @Test
    public void testCourseProposalEmpty() {
        UserEntity user = new UserEntity();
        user.setOpenid("1");
        user.setUserName("aaa");

        UserEntity saved = userRepository.save(user);

        WeChatUserInfo userInfo = new WeChatUserInfo();
        userInfo.setOpenId(saved.getOpenid());

        List<CourseRegisterBean> course = courseProposalService.proposeCourse(userInfo);
        Assert.assertEquals(0, course.size());

    }

    @Test
    public void testCourseProposal(){
        UserEntity user = new UserEntity();
        user.setOpenid(System.currentTimeMillis()+"");
        user.setUserName("bbb");

        UserEntity saved = userRepository.save(user);
        loginHistoryService.saveLogin(saved.getUserId());
        Assert.assertEquals(1, loginHistoryService.getDayNumber(saved.getUserId()));

        //TODO
    }
}
