package com.education.service;

import com.education.db.entity.CommonStatus;
import com.education.db.entity.CourseEntity;
import com.education.db.jpa.CourseRepository;
import com.education.db.jpa.CourseTypeRepository;
import com.education.db.jpa.UserRepository;
import com.education.formbean.CourseTagBean;
import com.education.ws.CourseRegisterBean;
import com.education.ws.util.WSUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

/**
 * Created by yzzhao on 12/15/15.
 */
@Service("CourseProposalService")
public class CourseProposalService {

    private static final Logger logger = Logger.getLogger(CourseProposalService.class.getName());

    @Autowired
    private CourseTypeRepository courseTypeRepository;

    @Autowired
    private LoginHistoryService loginHistoryService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private CourseTagService courseTagService;

    @Autowired
    private WSUtility wsUtility;

    public List<CourseRegisterBean> queryCourse(WeChatUserInfo userInfo, int tagId, String status, int number) {
        List<CourseEntity> courseList = new ArrayList<>();
        Date now = Calendar.getInstance().getTime();
        if (tagId <= 0) {
            courseList = courseRepository.findEnabledCoursesByStatus(CommonStatus.valueOf(status), now);
        } else {
            courseList = courseRepository.findEnabledCoursesByStatusAndCourseTag(CommonStatus.valueOf(status), tagId, now);
        }
        List<CourseRegisterBean> courseBeanList = new ArrayList<>();
        int index = 0;
        for (CourseEntity entity : courseList) {
            logger.info("add course " + entity.getId());
            CourseRegisterBean bean = new CourseRegisterBean(entity, wsUtility);
            courseBeanList.add(bean);
            StringBuilder builder = new StringBuilder();
            List<CourseTagBean> tags = courseTagService.getCourseTagsByCourseId(entity.getId());
            for (CourseTagBean tag : tags) {
                builder.append(tag.getName()).append(",");
            }
            String tagStr = builder.toString();
            if (tagStr.endsWith(",")) {
                tagStr = tagStr.substring(0, tagStr.length() - 1);
            }
            bean.setTags(tagStr);
            index++;
            if (number > 0 && index == number) {
                break;
            }
        }
        return courseBeanList;
    }


}
