package com.education.service;

import com.github.springtestdbunit.annotation.DatabaseSetup;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by yzzhao on 1/24/16.
 */
@Transactional
public class UserServiceTest extends AbstractServiceTest {

    @Autowired
    private UserService userService;

    @Test
    @DatabaseSetup(value = "classpath:/com/education/service/user_service_test.xml")
    public void testGetUserInfo(){
        WeChatUserInfo userInfo = userService.getUserInfo(2001);
        Assert.assertEquals("2001", userInfo.getUnionid());
        Assert.assertEquals("c2", userInfo.getNickname());
    }
}
