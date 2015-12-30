package com.education.service;

import com.education.db.entity.CommonStatus;
import com.education.db.entity.CourseEntity;
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

    public List<CourseRegisterBean> queryCourse(WeChatUserInfo userInfo, int categoryId, String status) {
        List<CourseEntity> courseList = courseRepository.findEnabledCoursesByStatusAndCategory(CommonStatus.valueOf(status), categoryId);
        List<CourseRegisterBean> courseBeanList = new ArrayList<>();
        for (CourseEntity entity : courseList) {
            CourseRegisterBean bean = new CourseRegisterBean(entity, wsUtility);
            courseBeanList.add(bean);
        }
        return courseBeanList;
    }


}
