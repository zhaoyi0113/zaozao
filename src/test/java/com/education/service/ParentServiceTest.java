package com.education.service;

import com.education.exception.BadRequestException;
import com.education.formbean.UserChildrenRegisterBean;
import com.education.ws.util.WSUtility;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;

/**
 * Created by yzzhao on 1/22/16.
 */
@Transactional
public class ParentServiceTest extends AbstractServiceTest{

    @Autowired
    private ParentService parentService;

    @Test
    @DatabaseSetup(value = "classpath:/com/education/service/parent_service_test.xml")
    public void testParentService(){
        UserChildrenRegisterBean child = new UserChildrenRegisterBean();
        child.setAge(10);
        child.setChildName("aaa");
        child.setChildBirthdate(WSUtility.dateToString(Calendar.getInstance().getTime()));
        WeChatUserInfo userInfo = new WeChatUserInfo();
        userInfo.setUnionid("3000");
        userInfo.setUserId(3000);
        parentService.updateUserProfile(null, child, userInfo.getUserId());

        UserChildrenRegisterBean userChild = parentService.getUserChild(userInfo.getUserId());
        Assert.assertEquals(10, userChild.getAge());

        userChild.setAge(11);
        parentService.updateUserProfile(null, userChild, userInfo.getUserId());
        userChild = parentService.getUserChild(userInfo.getUserId());
        Assert.assertEquals(11, userChild.getAge());

        parentService.deleteUserChild(userInfo.getUserId());

    }
}
