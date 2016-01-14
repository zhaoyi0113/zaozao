package com.education.service;

import com.education.db.entity.COURSE_ACCESS_FLAG;
import com.education.db.entity.UserCourseHistoryEntity;
import com.education.db.entity.UserEntity;
import com.education.db.jpa.UserCourseHistoryRepository;
import com.education.db.jpa.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.List;

/**
 * Created by yzzhao on 1/10/16.
 */
@Service("UserCourseHistoryService")
public class UserCourseHistoryService {

    @Autowired
    private UserCourseHistoryRepository historyRepository;

    @Autowired
    private UserRepository userRepository;

    @Transactional
    public void saveUserAccessHistory(UserEntity userInfo, int courseId, COURSE_ACCESS_FLAG flag) {
        UserCourseHistoryEntity entity = null;
        if (userInfo == null) {
            entity = new UserCourseHistoryEntity();
            entity.setUserId(0);
        } else {
            List<UserEntity> userList = userRepository.findByUnionid(userInfo.getUnionid());
            if (!userList.isEmpty()) {
                entity = new UserCourseHistoryEntity();
                entity.setUserId(userList.get(0).getUserId());
            }
        }
        if (entity != null) {
            entity.setAccessFlag(flag);
            entity.setCourseId(courseId);
            entity.setTimeCreated(Calendar.getInstance().getTime());
            historyRepository.save(entity);
        }
    }


}
