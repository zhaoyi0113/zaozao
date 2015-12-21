package com.education.service;

import com.education.db.entity.LoginHistoryEntity;
import com.education.db.entity.UserEntity;
import com.education.db.jpa.LoginHistoryRepository;
import com.education.db.jpa.UserRepository;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;

/**
 * Created by yzzhao on 12/21/15.
 */
@Transactional
public class LoginHistoryServiceTest extends AbstractServiceTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LoginHistoryService loginHistoryService;

    @Autowired
    private LoginHistoryRepository loginHistoryRepository;

    @Test
    public void testLoginHistory() {
        UserEntity user = new UserEntity();
        user.setAge(10);
        user.setUserName("username");
        UserEntity savedUser = userRepository.save(user);
        int id = loginHistoryService.saveLogin(savedUser.getUserId());
        LoginHistoryEntity lastLoginTime = loginHistoryService.getLastLoginTime(savedUser.getUserId());
        Assert.assertEquals(id, lastLoginTime.getId());
    }

    @Test
    public void testGetLastLoginTime() throws InterruptedException {
        UserEntity user = new UserEntity();
        user.setAge(10);
        user.setUserName("username");
        UserEntity savedUser = userRepository.save(user);
        int first = loginHistoryService.saveLogin(savedUser.getUserId());
        Thread.sleep(1000);
        int second = loginHistoryService.saveLogin(savedUser.getUserId());
        Thread.sleep(1000);
        int third = loginHistoryService.saveLogin(savedUser.getUserId());
        Thread.sleep(1000);
        int fouth = loginHistoryService.saveLogin(savedUser.getUserId());

        LoginHistoryEntity lastLoginTime = loginHistoryService.getLastLoginTime(savedUser.getUserId());
        Assert.assertEquals(fouth, lastLoginTime.getId());
        int loginCount = loginHistoryService.getLoginCount(savedUser.getUserId());
        Assert.assertEquals(4, loginCount);
    }

    @Test
    public void testGetDayNumber(){
        LoginHistoryEntity entity1 = new LoginHistoryEntity();
        entity1.setUserid(0);
        Calendar instance = Calendar.getInstance();
        instance.set(2015, 11, 2);
        entity1.setLoginTime(instance.getTime());
        loginHistoryRepository.save(entity1);

        LoginHistoryEntity entity2 = new LoginHistoryEntity();
        entity2.setUserid(0);
         instance = Calendar.getInstance();
        instance.set(2015, 11, 3);
        entity2.setLoginTime(instance.getTime());
        loginHistoryRepository.save(entity2);

        LoginHistoryEntity entity3 = new LoginHistoryEntity();
        entity3.setUserid(0);
        instance = Calendar.getInstance();
        instance.set(2015, 11, 3);
        entity3.setLoginTime(instance.getTime());
        loginHistoryRepository.save(entity3);

        LoginHistoryEntity entity4 = new LoginHistoryEntity();
        entity4.setUserid(0);
        instance = Calendar.getInstance();
        instance.set(2015, 10, 3);
        entity4.setLoginTime(instance.getTime());
        loginHistoryRepository.save(entity4);

        LoginHistoryEntity entity5 = new LoginHistoryEntity();
        entity5.setUserid(0);
        instance = Calendar.getInstance();
        instance.set(2013, 10, 3);
        entity5.setLoginTime(instance.getTime());
        loginHistoryRepository.save(entity5);

        LoginHistoryEntity entity6 = new LoginHistoryEntity();
        entity6.setUserid(0);
        instance = Calendar.getInstance();
        instance.set(2015, 10, 3);
        entity6.setLoginTime(instance.getTime());
        loginHistoryRepository.save(entity6);

        int dayNumber = loginHistoryService.getDayNumber(0);
        Assert.assertEquals(4, dayNumber);

    }
}
