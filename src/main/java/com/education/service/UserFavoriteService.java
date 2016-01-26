package com.education.service;

import com.education.db.entity.CourseEntity;
import com.education.db.entity.UserEntity;
import com.education.db.entity.UserFavoriteEntity;
import com.education.db.jpa.UserFavoriteRepository;
import com.education.db.jpa.UserRepository;
import com.education.exception.BadRequestException;
import com.education.exception.ErrorCode;
import com.education.formbean.CourseQueryBean;
import com.education.formbean.CourseTagBean;
import com.education.ws.util.WSUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.logging.Logger;

/**
 * Created by yzzhao on 1/22/16.
 */
@Service("UserFavoriteService")
public class UserFavoriteService {

    private static final Logger logger = Logger.getLogger(UserFavoriteService.class.getName());

    @Autowired
    private UserFavoriteRepository userFavoriteRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private WSUtility wsUtility;

    @Autowired
    private CourseTagService tagService;

    @Transactional
    public boolean addFavorite(int userId, int courseId) {
        UserEntity userEntity = userRepository.findOne(userId);
        if (userEntity == null) {
            throw new BadRequestException(ErrorCode.NOT_LOGIN);
        }
        if (whetherAddFavorite(userId, courseId)) {
            logger.info("delete user favorite userId=" + userId + ", courseId=" + courseId);
            userFavoriteRepository.deleteByUserIdAndCourseId(userId, courseId);
            return false;
        } else {
            logger.info("add user favorite userId=" + userId + ", courseId=" + courseId);
            UserFavoriteEntity entity = new UserFavoriteEntity();
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

    public List<CourseQueryBean> getUserFavoriteCourses(int userId, int pageIdx, int number) {
        UserEntity user = userRepository.findOne(userId);
        if (user == null) {
            throw new BadRequestException(ErrorCode.NOT_LOGIN);
        }
        List<CourseEntity> courses = userFavoriteRepository.findCoursesByUserId(userId);
        List<CourseQueryBean> courseQuery = new ArrayList<>();
        for (int i = pageIdx * number; i < courses.size(); i++) {
            CourseEntity entity = courses.get(i);
            CourseQueryBean bean = new CourseQueryBean(entity, wsUtility);
            courseQuery.add(bean);
            List<CourseTagBean> tags = tagService.getCourseTagsByCourseId(entity.getId());
            bean.setTags(tags);
            if (courseQuery.size() >= number) {
                break;
            }
        }
        return courseQuery;
    }

    public boolean whetherAddFavorite(int userId, int courseId) {
        List<UserFavoriteEntity> favoriteEntity = userFavoriteRepository.findByUserIdAndCourseId(userId, courseId);
        return !favoriteEntity.isEmpty();
    }

    @Transactional
    public void removeCourse(int courseId) {
        userFavoriteRepository.deleteByCourseId(courseId);
    }

}
