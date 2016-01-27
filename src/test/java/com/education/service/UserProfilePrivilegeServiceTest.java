package com.education.service;

import com.education.formbean.UserProfilePrivilegeBean;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by yzzhao on 1/27/16.
 */
@Transactional
public class UserProfilePrivilegeServiceTest extends AbstractServiceTest {

    @Autowired
    private UserProfilePrivilegeService service;

    @Test
    @DatabaseSetup(value = "classpath:/com/education/service/user_profile_privilege_service_test.xml")
    public void testPrivilegeService() {
        UserProfilePrivilegeBean pri = service.getUserProfilePrivilege();
        Assert.assertEquals(1, pri.getChildBirthdate());
        Assert.assertEquals(1, pri.getUserImage());
        Assert.assertEquals(1, pri.getUserName());
        Assert.assertEquals(1, pri.getChildName());

        pri.setChildBirthdate(0);
        service.editUserProfilePrivilege(pri);
        pri = service.getUserProfilePrivilege();
        Assert.assertEquals(0, pri.getChildBirthdate());

    }
}
