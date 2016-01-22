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
        int id = parentService.registerUserChild(child, userInfo);

        UserChildrenRegisterBean userChild = parentService.getUserChild(userInfo);
        Assert.assertEquals(id, userChild.getId());
        Assert.assertEquals(10, userChild.getAge());

        userChild.setAge(11);
        parentService.editUserChild(userChild);
        userChild = parentService.getUserChild(userInfo);
        Assert.assertEquals(id, userChild.getId());
        Assert.assertEquals(11, userChild.getAge());

        parentService.deleteUserChild(userInfo);

        BadRequestException ex=null;
        try {
            parentService.getUserChild(userInfo);
        }catch(BadRequestException e){
            ex=e;
        }
        Assert.assertNotNull(ex);
    }
}
