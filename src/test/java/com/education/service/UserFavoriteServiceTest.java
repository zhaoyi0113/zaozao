package com.education.service;

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
        favoriteService.addFavorite("2000", 2000);

        count = favoriteService.getTotalFavoriteCount(2000);
        Assert.assertEquals(2, count);

    }


    @Test
    @DatabaseSetup(value = "classpath:/com/education/service/user_favorite_service_test.xml")
    public void testIsFavoriteCourse() {
        boolean b = favoriteService.whetherAddFavorite("2001", 2000);
        Assert.assertTrue(b);
        b = favoriteService.whetherAddFavorite("2001", 2001);
        Assert.assertTrue(b);
        b = favoriteService.whetherAddFavorite("2001", 2002);
        Assert.assertFalse(b);

    }

    @Test
    @DatabaseSetup(value = "classpath:/com/education/service/user_favorite_service_test.xml")
    public void testGetUserFavoritedCourses(){
        List<CourseQueryBean> courses = favoriteService.getUserFavoriteCourses("2001");
        Assert.assertEquals(3, courses.size());
        courses = favoriteService.getUserFavoriteCourses("2000");
        Assert.assertEquals(3, courses.size());

    }
}
