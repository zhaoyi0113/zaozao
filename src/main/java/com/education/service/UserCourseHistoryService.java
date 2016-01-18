package com.education.service;

import com.education.db.entity.COURSE_ACCESS_FLAG;
import com.education.db.entity.CourseEntity;
import com.education.db.entity.UserCourseHistoryEntity;
import com.education.db.entity.UserEntity;
import com.education.db.jpa.CourseRepository;
import com.education.db.jpa.UserCourseHistoryRepository;
import com.education.db.jpa.UserRepository;
import com.education.formbean.CourseUserAnalyticsBean;
import com.education.service.converter.WeChatUserConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
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

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private WeChatUserConverter weChatUserConverter;

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

        CourseEntity course = courseRepository.findOne(courseId);
        if(course != null){
            course.setPv(course.getPv()+1);
            courseRepository.save(course);
        }
    }


    public List<CourseUserAnalyticsBean> getCourseUserAnalytics(int courseId, int pageIdx, int number) {
        PageRequest pageRequest = new PageRequest(pageIdx, number, new Sort(Sort.Direction.DESC, "timeCreated"));
        List<UserCourseHistoryEntity> courses = historyRepository.findByCourseId(courseId, pageRequest);
        List<CourseUserAnalyticsBean> beans = new ArrayList<>();
        for (UserCourseHistoryEntity entity : courses) {
            CourseUserAnalyticsBean bean = new CourseUserAnalyticsBean();
            UserEntity user = userRepository.findOne(entity.getUserId());
            if (user != null) {
                WeChatUserInfo weChatUserInfo = weChatUserConverter.convert(user);
                bean.setUserInfo(weChatUserInfo);
            }
            bean.setAccessDate(entity.getTimeCreated());
            bean.setFlag(entity.getAccessFlag());
            beans.add(bean);
        }
        return beans;
    }
}
