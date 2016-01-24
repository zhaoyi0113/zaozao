package com.education.service;

import com.education.db.entity.CourseEntity;
import com.education.db.entity.UserEntity;
import com.education.db.entity.UserFavoriteEntity;
import com.education.db.jpa.UserFavoriteRepository;
import com.education.db.jpa.UserRepository;
import com.education.exception.BadRequestException;
import com.education.exception.ErrorCode;
import com.education.formbean.CourseQueryBean;
import com.education.ws.util.WSUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by yzzhao on 1/22/16.
 */
@Service("UserFavoriteService")
public class UserFavoriteService {

    @Autowired
    private UserFavoriteRepository userFavoriteRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private WSUtility wsUtility;

    @Transactional
    public boolean addFavorite(String unionId, int courseId) {
        List<UserEntity> users = userRepository.findByUnionid(unionId);
        if (users.isEmpty()) {
            throw new BadRequestException(ErrorCode.NOT_LOGIN);
        }
        if(whetherAddFavorite(users.get(0).getUnionid(), courseId)){
            userFavoriteRepository.deleteByUserIdAndCourseId(users.get(0).getUserId(), courseId);
            return false;
        }else {
            UserFavoriteEntity entity = new UserFavoriteEntity();
            UserEntity userEntity = users.get(0);
            entity.setUserId(userEntity.getUserId());
            entity.setCourseId(courseId);
            entity.setTimeCreated(Calendar.getInstance().getTime());
            userFavoriteRepository.save(entity);
            return true;
        }
    }

    public int getTotalFavoriteCount(int courseId) {
        return userFavoriteRepository.getCourseFavoriteCount(courseId);
    }

    public List<CourseQueryBean> getUserFavoriteCourses(String unionId) {
        List<UserEntity> users = userRepository.findByUnionid(unionId);
        if (users.isEmpty()) {
            throw new BadRequestException(ErrorCode.NOT_LOGIN);
        }
        List<CourseEntity> courses = userFavoriteRepository.findCoursesByUserId(users.get(0).getUserId());
        List<CourseQueryBean> courseQuery = new ArrayList<>();
        for (CourseEntity entity : courses) {
            CourseQueryBean bean = new CourseQueryBean(entity, wsUtility);
            courseQuery.add(0, bean);
        }
        return courseQuery;
    }

    public boolean whetherAddFavorite(String unionId, int courseId) {
        List<UserEntity> users = userRepository.findByUnionid(unionId);
        if (users.isEmpty()) {
            throw new BadRequestException(ErrorCode.NOT_LOGIN);
        }
        List<UserFavoriteEntity> favoriteEntity = userFavoriteRepository.findByUserIdAndCourseId(users.get(0).getUserId(), courseId);
        return !favoriteEntity.isEmpty();
    }

}
