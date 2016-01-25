package com.education.service;

import com.education.db.entity.CommonStatus;
import com.education.db.entity.CourseEntity;
import com.education.db.entity.CourseTagEntity;
import com.education.db.entity.CourseTagRelationEntity;
import com.education.db.jpa.CourseRepository;
import com.education.db.jpa.CourseTagRelationRepository;
import com.education.db.jpa.CourseTagRepository;
import com.education.exception.BadRequestException;
import com.education.exception.ErrorCode;
import com.education.formbean.CourseQueryBean;
import com.education.formbean.CourseTagBean;
import com.education.formbean.CourseUserAnalyticsBean;
import com.education.ws.CourseRegisterBean;
import com.education.ws.util.WSUtility;
import jersey.repackaged.com.google.common.collect.Lists;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by yzzhao on 12/5/15.
 */
@Service("CourseService")
public class CourseService {

    private static final Logger logger = Logger.getLogger(CourseService.class.getName());

    private static final String COURSE_TAG_SEPARATOR = ",";

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private WSUtility wsUtility;

    @Autowired
    private CourseTagRepository courseTagRepository;

    @Autowired
    private CourseTagRelationRepository courseTagRelationRepository;

    @Autowired
    private UserCourseHistoryService userCourseHistoryService;

    @Autowired
    private CourseTagService tagService;

    @Value("#{config['course_image_path']}")
    private String courseImagePath;

    @Autowired
    private UserFavoriteService favoriteService;

    @Transactional
    public int createCourse(CourseRegisterBean bean) {
        CourseEntity entity = new CourseEntity(bean);
        entity.setTimeCreated(Calendar.getInstance().getTime());
        CourseEntity save = courseRepository.save(entity);
        saveCourseTags(save.getId(), bean.getTags());
        return save.getId();
    }

    private void saveCourseTags(int courseId, String tags) {
        if (tags == null) {
            return;
        }
        String[] tagsId = null;
        if (tags.contains(COURSE_TAG_SEPARATOR)) {
            tagsId = tags.trim().split(COURSE_TAG_SEPARATOR);
        } else {
            tagsId = new String[]{tags.trim()};
        }
        for (int i = 0; i < tagsId.length; i++) {
            CourseTagRelationEntity tagEntity = new CourseTagRelationEntity();
            tagEntity.setCourseId(courseId);
            tagEntity.setCourseTagId(Integer.parseInt(tagsId[i]));
            courseTagRelationRepository.save(tagEntity);
        }
    }

    public FileInputStream getCourseFile(String id, String fileName) {
        CourseEntity course = courseRepository.findOne(Integer.parseInt(id));
        if (course == null) {
            throw new BadRequestException(ErrorCode.COURSE_NOT_FOUND);
        }
        String videoPath = wsUtility.getResourcePhysicalPath(fileName);
        try {
            FileInputStream input = new FileInputStream(videoPath);
            return input;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            throw new BadRequestException(ErrorCode.FILE_NOT_EXISTED);
        }
    }

    public List<CourseQueryBean> queryCourses(int number, int pageIdx) {
        Iterable<CourseEntity> courseEntityList = courseRepository.findAllByOrderByTimeCreatedDesc();
        List<CourseEntity> courseList = Lists.newArrayList(courseEntityList);
        List<CourseQueryBean> courseBeanList = new ArrayList<>();
        for (int i = number * pageIdx; i < courseList.size(); i++) {
            CourseEntity entity = courseList.get(i);
            logger.info("add course " + entity.getId());
            List<CourseTagBean> courseTags = tagService.getCourseTagsByCourseId(entity.getId());
            CourseQueryBean bean = new CourseQueryBean(entity, wsUtility);
            bean.setTags(courseTags);
            courseBeanList.add(bean);
            if (number > 0 && courseBeanList.size() >= number) {
                break;
            }
        }
        return courseBeanList;
    }

    public long getCourseCount() {
        return courseRepository.count();
    }

    public List<CourseQueryBean> getAllCoursesIndex() {
        try {
            Iterable<CourseEntity> courseIterable = courseRepository.findAll();
            List<CourseQueryBean> queryList = new ArrayList<>();
            for (CourseEntity entity : courseIterable) {
                List<CourseTagBean> courseTagList = getCourseTagList(entity.getId());
                CourseQueryBean queryBean = new CourseQueryBean(entity, wsUtility);
                queryBean.setTags(courseTagList);
                queryList.add(queryBean);
            }
            return queryList;
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
        }
        return null;
    }

    private List<CourseTagBean> getCourseTagList(int courseId) {
        List<CourseTagRelationEntity> courseTags = courseTagRelationRepository.findCourseTagsByCourseId(courseId);
        List<CourseTagBean> tagBean = new ArrayList<>();
        for (CourseTagRelationEntity tag : courseTags) {
            CourseTagEntity courseTag = courseTagRepository.findOne(tag.getCourseTagId());
            CourseTagBean bean = new CourseTagBean(courseTag.getId(), courseTag.getName());
            tagBean.add(bean);
        }
        return tagBean;
    }

    public CourseQueryBean queryCourse(String courseId) {
        int id = Integer.parseInt(courseId);

        CourseEntity course = courseRepository.findOne(id);// getCourseDao().getCourseById(id);
        if (course == null) {
            throw new BadRequestException(ErrorCode.COURSE_NOT_FOUND);
        }
        CourseQueryBean queryBean = new CourseQueryBean(course, wsUtility);
        queryBean.setTags(getCourseTagList(course.getId()));
        return queryBean;
    }

    @Transactional
    public void editCourse(CourseRegisterBean bean) {
        CourseEntity one = courseRepository.findOne(Integer.parseInt(bean.getId()));
        one.setContent(bean.getContent());
        one.setIntroduction(bean.getIntroduction());
        one.setName(bean.getName());
        if (bean.getTitleImagePath() != null) {
            one.setTitleImagePath(bean.getTitleImagePath());
        }
        one.setVideoExternalUrl(bean.getVideoExternalUrl());
        one.setStatus(CommonStatus.valueOf(bean.getStatus()));
        one.setPublishDate(wsUtility.stringToDate(bean.getPublishDate()));
        one.setVideoLength(bean.getVideoLength());
        one.setPv(bean.getPv());
        courseRepository.save(one);
        courseTagRelationRepository.removeByCourseId(one.getId());
        saveCourseTags(one.getId(), bean.getTags());
    }

    @Transactional
    public void deleteCourse(int courseId) {
        CourseEntity course = courseRepository.findOne(courseId);
        if (course == null) {
            throw new com.education.exception.BadRequestException(ErrorCode.COURSE_NOT_FOUND);
        }
        courseRepository.delete(course);
        if (course.getTitleImagePath() != null) {
            String filePath = courseImagePath + "/" + course.getId();
            deleteFile(filePath);
        }
        courseTagRelationRepository.removeByCourseId(course.getId());
//        remove from favorite
        favoriteService.removeCourse(courseId);
    }

    private static void deleteFile(String filePath) {
        File file = new File(filePath);
        try {
            FileUtils.deleteDirectory(file);
        } catch (IOException e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
        }
    }

    public Map<Integer, String> findCourseIdAndNames() {
        List<Object[]> idNames = courseRepository.findIdAndNames();
        Map<Integer, String> courses = new Hashtable<>();
        for (Object[] in : idNames) {
            courses.put(Integer.parseInt(String.valueOf(in[0])), String.valueOf(in[1]));
        }
        return courses;
    }

    public List<CourseUserAnalyticsBean> getCourseUserAnalytics(int courseId, int pageIdx, int number) {
        return userCourseHistoryService.getCourseUserAnalytics(courseId, pageIdx, number);
    }
}
