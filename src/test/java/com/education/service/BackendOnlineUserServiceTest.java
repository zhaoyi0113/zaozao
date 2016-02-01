package com.education.service;

import com.github.springtestdbunit.annotation.DatabaseSetup;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by yzzhao on 1/15/16.
 */
@Transactional
public class BackendOnlineUserServiceTest extends AbstractServiceTest{

    @Autowired
    private BackendOnlineUserService userService;

    @Test
    @DatabaseSetup(value = "classpath:/com/education/service/backend_online_user_service_test.xml")
    public void testGetUserList(){
        List<OnlineUserInfo> userList = userService.getUserList(0, 5);
        Assert.assertEquals(5, userList.size());
        Assert.assertEquals(100010, userList.get(0).getUserId());
        userList = userService.getUserList(1, 5);
        Assert.assertEquals(5, userList.size());
        Assert.assertEquals(10005, userList.get(0).getUserId());
    }
}
