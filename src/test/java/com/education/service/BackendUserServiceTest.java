package com.education.service;

import com.education.db.entity.BackendLoginHistoryEntity;
import com.education.db.jpa.BackendLoginHistoryRepository;
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

    @Autowired
    private BackendLoginService loginService;

    @Autowired
    private BackendLoginHistoryRepository loginHistoryRepository;

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
        Assert.assertEquals(userId, allUsers.get(allUsers.size()-1).getId());

        String role = loginService.getUserRole("aaa", "1234");
        Assert.assertEquals(roleName, role);

        userService.deleteUser(userId);
        List<BackendUserBean> allUsers1 = userService.getAllUsers();
        Assert.assertEquals(allUsers.size(), allUsers1.size()+1);
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

    @Test
    public void testLogin(){
        String roleName = String.valueOf(System.currentTimeMillis());
        int roleId = roleService.createNewRole(roleName);
        BackendUserBean userBean = new BackendUserBean("aaa","1234",roleId);
        int userId = userService.createNewUser(userBean);
        boolean login = loginService.login("aaa", "1234");
        Assert.assertTrue(login);
        List<BackendLoginHistoryEntity> history = loginHistoryRepository.findByUserId(userId);
        Assert.assertFalse(history.isEmpty());
    }

    @Test
    public void testLoginFailed(){
        boolean login = loginService.login("aaa", "1234");
        Assert.assertFalse(login);
    }
}
