package com.education.service;

import com.education.db.jpa.BackendUserRepository;
import com.education.formbean.BackendRoleService;
import com.education.formbean.BackendUserBean;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by yzzhao on 12/26/15.
 */
@Transactional
public class BackendUserServiceTest extends AbstractServiceTest {

    @Autowired
    private BackendUserService userService;

    @Autowired
    private BackendUserRepository userRepository;

    @Autowired
    private BackendRoleService roleService;

    @Test
    public void testCreateNewUser(){
        String roleName = String.valueOf(System.currentTimeMillis());
        int roleId = roleService.createNewRole(roleName);
        BackendUserBean userBean = new BackendUserBean("aaa","1234",roleId);
        int userId = userService.createNewUser(userBean);
        BackendUserBean userBean1 = userService.queryUser(userId);
        Assert.assertNotNull(userBean1);
        Assert.assertEquals(userId, userBean1.getId());
        Assert.assertEquals("aaa", userBean1.getName());
        Assert.assertEquals(roleId, userBean1.getRoleId());
        List<BackendUserBean> allUsers = userService.getAllUsers();
        Assert.assertEquals(1, allUsers.size());
        Assert.assertEquals(userId, allUsers.get(0).getId());

        userService.deleteUser(userId);
        allUsers = userService.getAllUsers();
        Assert.assertTrue(allUsers.isEmpty());
    }

    @Test
    public void testEditUser(){
        String roleName = String.valueOf(System.currentTimeMillis());
        int roleId = roleService.createNewRole(roleName);
        BackendUserBean userBean = new BackendUserBean("aaa","1234",roleId);
        int userId = userService.createNewUser(userBean);
        userBean.setName("bbb");
        userBean.setId(userId);
        userService.editUser(userBean);
        BackendUserBean userBean1 = userService.queryUser(userId);
        Assert.assertEquals("bbb", userBean1.getName());
    }

}
