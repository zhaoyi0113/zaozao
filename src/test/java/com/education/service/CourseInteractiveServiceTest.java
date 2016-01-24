package com.education.service;

import com.education.db.entity.COURSE_ACCESS_FLAG;
import com.education.db.entity.UserCourseHistoryEntity;
import com.education.db.entity.UserEntity;
import com.education.db.jpa.UserCourseHistoryRepository;
import com.education.db.jpa.UserRepository;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by yzzhao on 1/14/16.
 */
@Transactional
public class CourseInteractiveServiceTest extends AbstractServiceTest {

    @Autowired
    private CourseInteractiveService interactiveService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserCourseHistoryRepository historyRepository;

    @Test
    @DatabaseSetup(value="classpath:/com/education/service/course_analyse_service_test.xml")
    public void activeCourseTest(){
        UserEntity userEntity = userRepository.findOne(30000);

        interactiveService.activeCourse(userEntity, 1000, COURSE_ACCESS_FLAG.SHARE.name());
        List<UserCourseHistoryEntity> userList = historyRepository.findByUserId(userEntity.getUserId());
        UserCourseHistoryEntity matchEntity = null;
        for(UserCourseHistoryEntity entity: userList){
            if(entity.getCourseId() == 1000 && entity.getAccessFlag().equals(COURSE_ACCESS_FLAG.SHARE)){
                matchEntity = entity;
                break;
            }
        }
        Assert.assertNotNull(matchEntity);
        UserEntity userEntity1 = new UserEntity();
        userEntity1.setUnionid("30000");
        userEntity1.setUserId(30000);
        interactiveService.activeCourse(userEntity1, 1000, COURSE_ACCESS_FLAG.FAVORITE.name());
        userList = historyRepository.findByCourseId(1000);
        matchEntity = null;
        for(UserCourseHistoryEntity entity: userList){
            if(entity.getCourseId() == 1000 && entity.getAccessFlag().equals(COURSE_ACCESS_FLAG.FAVORITE)){
                matchEntity = entity;
                break;
            }
        }
        Assert.assertNotNull(matchEntity);
    }

}
