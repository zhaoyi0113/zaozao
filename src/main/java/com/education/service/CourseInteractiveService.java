package com.education.service;

import com.education.db.entity.COURSE_ACCESS_FLAG;
import com.education.db.entity.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by yzzhao on 1/13/16.
 */
@Service
public class CourseInteractiveService {

    private static final Logger logger = Logger.getLogger(CourseProposalService.class.getName());

    @Autowired
    private UserCourseHistoryService historyService;

    @Autowired
    private UserFavoriteService favoriteService;

    @Transactional
    public void activeCourse(UserEntity userEntity, int courseId, String flag) {
        try {
            COURSE_ACCESS_FLAG courseAccessFlag = COURSE_ACCESS_FLAG.valueOf(flag);
            if(courseAccessFlag.equals(COURSE_ACCESS_FLAG.FAVORITE)){
                favoriteService.addFavorite(userEntity.getUnionid(), courseId);
            }
            historyService.saveUserAccessHistory(userEntity, courseId, courseAccessFlag);
        } catch (IllegalArgumentException e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
        }
    }
}
