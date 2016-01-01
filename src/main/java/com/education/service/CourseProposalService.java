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

import java.util.*;
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
        List<CourseEntity> courseList = getCourseEntities(tagId, status);
        List<CourseRegisterBean> courseBeanList = new ArrayList<>();
        for (CourseEntity entity : courseList) {
            logger.info("add course " + entity.getId());
            CourseRegisterBean bean = new CourseRegisterBean(entity, wsUtility);
            String tagStr = getCourseTagString(entity);
            bean.setTags(tagStr);
            courseBeanList.add(bean);
            if (number > 0 && courseBeanList.size() >= number) {
                break;
            }
        }
        return courseBeanList;
    }

    private String getCourseTagString(CourseEntity entity) {
        StringBuilder builder = new StringBuilder();
        List<CourseTagBean> tags = courseTagService.getCourseTagsByCourseId(entity.getId());
        for (CourseTagBean tag : tags) {
            builder.append(tag.getName()).append(",");
        }
        String tagStr = builder.toString();
        if (tagStr.endsWith(",")) {
            tagStr = tagStr.substring(0, tagStr.length() - 1);
        }
        return tagStr;
    }

    public Map<String, List<CourseRegisterBean>> queryCourseByDate(WeChatUserInfo userInfo, int tagId, String status, int number) {
        List<CourseEntity> courseList = getCourseEntities(tagId, status);
        Map<Date, List<CourseRegisterBean>> courseMap = new Hashtable<>();
        int index = 0;
        for (CourseEntity entity : courseList) {
            Date publishDate = entity.getPublishDate();
            List<CourseRegisterBean> beanList = null;
            if (courseMap.containsKey(publishDate)) {
                beanList = courseMap.get(publishDate);
            } else {
                beanList = new ArrayList<>();
                courseMap.put(publishDate, beanList);
            }
            CourseRegisterBean cbean = new CourseRegisterBean(entity, wsUtility);
            cbean.setTags(getCourseTagString(entity));
            beanList.add(cbean);
            index++;
            if (number > 0 && index >= number) {
                break;
            }
        }
        Map<Date, List<CourseRegisterBean>> sortedMap = new TreeMap<>(new Comparator<Date>() {
            @Override
            public int compare(Date o1, Date o2) {
                return o1.compareTo(o2);
            }
        });
        sortedMap.putAll(courseMap);
        Map<String, List<CourseRegisterBean>> sortedCourses = new LinkedHashMap<>();
        for (Map.Entry<Date, List<CourseRegisterBean>> entry : sortedMap.entrySet()) {
            String dateStr = WSUtility.dateToString(entry.getKey());
            sortedCourses.put(dateStr, entry.getValue());
        }
        return sortedCourses;
    }

    private List<CourseEntity> getCourseEntities(int tagId, String status) {
        List<CourseEntity> courseList;
        Date now = Calendar.getInstance().getTime();
        if (tagId <= 0) {
            courseList = courseRepository.findEnabledCoursesByStatus(CommonStatus.valueOf(status), now);
        } else {
            courseList = courseRepository.findEnabledCoursesByStatusAndCourseTag(CommonStatus.valueOf(status), tagId, now);
        }
        return courseList;
    }
}
