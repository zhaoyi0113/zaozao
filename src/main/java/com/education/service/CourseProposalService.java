package com.education.service;

import com.education.db.entity.CourseEntity;
import com.education.db.entity.CourseTypeEntity;
import com.education.db.entity.UserEntity;
import com.education.db.jpa.CourseRepository;
import com.education.db.jpa.CourseTypeRepository;
import com.education.db.jpa.UserRepository;
import com.education.ws.CourseRegisterBean;
import com.education.ws.util.WSUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yzzhao on 12/15/15.
 */
@Service("CourseProposalService")
public class CourseProposalService {

    private static final String YUJIAO_COURSE = "预教课程";
    private static final String PIC_READING = "绘本阅读";
    private static final String PARTERNITY_YOGA = "亲子瑜伽";
    private static final String ART_COURSE = "艺术课程";
    private static final String INFANT_SUPPLIES = "母婴用品";

    @Autowired
    private CourseTypeRepository courseTypeRepository;

    @Autowired
    private LoginHistoryService loginHistoryService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private WSUtility wsUtility;

    public List<CourseRegisterBean> proposeCourse(WeChatUserInfo userInfo) {
        List<UserEntity> user = userRepository.findByOpenid(userInfo.getOpenId());
        int dayNumber = loginHistoryService.getDayNumber(user.get(0).getUserId());
        return proposeCourse(userInfo, dayNumber);
    }

    private List<CourseRegisterBean> proposeCourse(WeChatUserInfo userInfo, int dayNumber) {
        List<CourseRegisterBean> courseList = new ArrayList<>();
        addCourseOnList(courseList, getCourse(dayNumber, YUJIAO_COURSE));
        addCourseOnList(courseList, getCourse(dayNumber, PIC_READING));
        addCourseOnList(courseList, getCourse(dayNumber, PARTERNITY_YOGA));
        addCourseOnList(courseList, getCourse(dayNumber, ART_COURSE));
        addCourseOnList(courseList, getCourse(dayNumber, INFANT_SUPPLIES));
        return courseList;
    }

    private CourseRegisterBean getCourse(int dayNumber, String courseTypeName) {
        CourseTypeEntity courseType = courseTypeRepository.findByName(courseTypeName);
        CourseEntity courseEntity = courseRepository.findByCategoryAndDayNumber(courseType.getId(), dayNumber + 1);
        if (courseEntity == null) {
            return null;
        }
        CourseRegisterBean bean = new CourseRegisterBean(courseEntity, wsUtility);
        return bean;
    }

    private void addCourseOnList(List<CourseRegisterBean> courseList, CourseRegisterBean bean) {
        if (bean != null) {
            courseList.add(bean);
        }
    }


}
