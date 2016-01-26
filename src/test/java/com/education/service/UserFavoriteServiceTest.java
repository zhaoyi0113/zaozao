package com.education.service;

import com.education.db.entity.UserEntity;
import com.education.formbean.CourseQueryBean;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by yzzhao on 1/22/16.
 */
@Transactional
public class UserFavoriteServiceTest extends AbstractServiceTest {

    @Autowired
    private UserFavoriteService favoriteService;

    @Autowired
    private CourseProposalService proposalService;

    @Test
    @DatabaseSetup(value = "classpath:/com/education/service/user_favorite_service_test.xml")
    public void testFavoriteService() {
        int count = favoriteService.getTotalFavoriteCount(2000);
        Assert.assertEquals(1, count);
        count = favoriteService.getTotalFavoriteCount(2001);
        Assert.assertEquals(1, count);
        count = favoriteService.getTotalFavoriteCount(2002);
        Assert.assertEquals(1, count);
        count = favoriteService.getTotalFavoriteCount(2003);
        Assert.assertEquals(3, count);

        WeChatUserInfo userInfo = new WeChatUserInfo();
        favoriteService.addFavorite(2000, 2000);

        count = favoriteService.getTotalFavoriteCount(2000);
        Assert.assertEquals(2, count);

    }


    @Test
    @DatabaseSetup(value = "classpath:/com/education/service/user_favorite_service_test.xml")
    public void testIsFavoriteCourse() {
        boolean b = favoriteService.whetherAddFavorite(2001, 2000);
        Assert.assertTrue(b);
        b = favoriteService.whetherAddFavorite(2001, 2001);
        Assert.assertTrue(b);
        b = favoriteService.whetherAddFavorite(2001, 2002);
        Assert.assertFalse(b);
        UserEntity userInfo = new UserEntity();
        userInfo.setUnionid("2000");
        userInfo.setUserId(2000);
        CourseQueryBean queryBean = proposalService.queryCourse(userInfo, 2000);
        Assert.assertFalse(queryBean.isFavorited());
        queryBean = proposalService.queryCourse(userInfo, 2002);
        Assert.assertTrue(queryBean.isFavorited());
        queryBean = proposalService.queryCourse(userInfo, 2003);
        Assert.assertTrue(queryBean.isFavorited());
    }

    @Test
    @DatabaseSetup(value = "classpath:/com/education/service/user_favorite_service_test.xml")
    public void testGetUserFavoritedCourses() {
        List<CourseQueryBean> courses = favoriteService.getUserFavoriteCourses(2001, 0, 100);
        Assert.assertEquals(3, courses.size());
        courses = favoriteService.getUserFavoriteCourses(2000, 0, 100);
        Assert.assertEquals(3, courses.size());

        courses = favoriteService.getUserFavoriteCourses(2001, 0, 1);
        Assert.assertEquals(1, courses.size());

        courses = favoriteService.getUserFavoriteCourses(2000, 1, 2);
        Assert.assertEquals(1, courses.size());
    }

    @Test
    @DatabaseSetup(value = "classpath:/com/education/service/user_favorite_service_test.xml")
    public void testGetUserFavoritedCoursesOrder() {
        boolean b = favoriteService.addFavorite(2002, 2003);
        Assert.assertTrue(b);
        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        favoriteService.addFavorite(2002, 2004);
        List<CourseQueryBean> courses = favoriteService.getUserFavoriteCourses(2002, 0, 10);
        Assert.assertEquals(2, courses.size());
        Assert.assertEquals("2004", courses.get(0).getId());
        Assert.assertEquals("2003", courses.get(1).getId());
        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        favoriteService.addFavorite(2002, 2001);
        courses = favoriteService.getUserFavoriteCourses(2002, 0, 10);
        Assert.assertEquals(3, courses.size());
        Assert.assertEquals("2001", courses.get(0).getId());
    }

    @Test
    @DatabaseSetup(value = "classpath:/com/education/service/user_favorite_service_test.xml")
    public void testRemoveFavorite() {
        boolean b = favoriteService.whetherAddFavorite(2001, 2000);
        Assert.assertTrue(b);
        favoriteService.addFavorite(2001, 2000);
        b = favoriteService.whetherAddFavorite(2001, 2000);
        Assert.assertFalse(b);
    }

    @Test
    @DatabaseSetup(value = "classpath:/com/education/service/user_favorite_service_test.xml")
    public void testRemoveCourse() {
        favoriteService.removeCourse(2000);
        int count = favoriteService.getTotalFavoriteCount(2000);
        Assert.assertEquals(0, count);

    }
}
