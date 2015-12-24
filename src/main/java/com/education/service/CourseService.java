package com.education.service;

import com.education.db.entity.CourseEntity;
import com.education.db.entity.CourseTagEntity;
import com.education.db.entity.CourseTagRelationEntity;
import com.education.db.entity.CourseTypeEntity;
import com.education.db.jpa.CourseRepository;
import com.education.db.jpa.CourseTagRelationRepository;
import com.education.db.jpa.CourseTagRepository;
import com.education.db.jpa.CourseTypeRepository;
import com.education.formbean.CourseQueryBean;
import com.education.ws.CourseRegisterBean;
import com.education.ws.util.WSUtility;
import jersey.repackaged.com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.ws.rs.BadRequestException;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
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
    private CourseTypeRepository courseTypeRepository;

    @Autowired
    private WSUtility wsUtility;

    @Autowired
    private CourseTagRepository courseTagRepository;

    @Autowired
    private CourseTagRelationRepository courseTagRelationRepository;

    public List<CourseRegisterBean> queryCourseByCategoryAfterNow(String category) {
        return queryCourseByCategory(category, true);
    }

    public List<CourseRegisterBean> queryCourseByCategoryBeforeNow(String category) {
        return queryCourseByCategory(category, false);
    }

    @Transactional
    public int createCourse(CourseRegisterBean bean) {
        List<CourseEntity> courses = courseRepository.findByName(bean.getName());
        if (courses != null && courses.size() > 0) {
            if (bean.getVideoPath() != null) {
                CourseEntity entity = courses.get(0);
                entity.setVideoPath(bean.getVideoPath());
                courseRepository.save(entity);
                return entity.getId();
            } else if (bean.getTitleImagePath() != null) {
                CourseEntity entity = courses.get(0);
                entity.setTitleImagePath(bean.getTitleImagePath());
                courseRepository.save(entity);
                return entity.getId();
            } else {
                throw new BadRequestException("duplicate course name " + bean.getName());
            }
        } else {
            CourseEntity entity = new CourseEntity(bean);
            entity.setTimeCreated(Calendar.getInstance().getTime());
            CourseEntity save = courseRepository.save(entity);
            saveCourseTags(save.getId(), bean.getTags());
            return save.getId();
        }
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
            throw new BadRequestException("Can't find course id " + id);
        }
        String videoPath = wsUtility.getResourcePhysicalPath(fileName);
        try {
            FileInputStream input = new FileInputStream(videoPath);
            return input;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            throw new BadRequestException(e.getMessage());
        }
    }

    public List<CourseQueryBean> getAllCoursesIndex() {
        try {
            Iterable<CourseEntity> courseIterable = courseRepository.findAll();
            Iterable<CourseTypeEntity> courseTypeIterable = courseTypeRepository.findAll();
            List<CourseQueryBean> queryList =new ArrayList<>();
            for (CourseEntity entity : courseIterable) {
                List<CourseTagEntity> courseTagList = getCourseTagList(entity.getId());
                CourseQueryBean queryBean = new CourseQueryBean(entity, wsUtility);
                queryList.add(queryBean);
            }
            return queryList;
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
        }
        return null;
    }

    private List<CourseTagEntity> getCourseTagList(int courseId){
        List<CourseTagRelationEntity> courseTags = courseTagRelationRepository.findCourseTagsByCourseId(courseId);
        List<CourseTagEntity> courseTagEntities = new ArrayList<>();
        for(CourseTagRelationEntity tag: courseTags){
            CourseTagEntity courseTag = courseTagRepository.findOne(tag.getCourseId());
            courseTagEntities.add(courseTag);
        }
        return courseTagEntities;
    }

    public CourseRegisterBean queryCourse(String courseId) {
        int id = Integer.parseInt(courseId);

        CourseEntity course = courseRepository.findOne(id);// getCourseDao().getCourseById(id);
        if (course == null) {
            throw new BadRequestException("can't find course " + courseId);
        }
        CourseRegisterBean bean = new CourseRegisterBean(course, wsUtility);
        CourseTypeEntity courseType = courseTypeRepository.findOne(course.getCategory());
        if (courseType != null) {
            bean.setCategory(courseType.getName());
        }
        return bean;
    }

    public void editCourse(CourseRegisterBean bean) {
        CourseEntity one = courseRepository.findOne(Integer.parseInt(bean.getId()));
        one.setContent(bean.getContent());
        one.setCategory(Integer.parseInt(bean.getCategory()));
        one.setIntroduction(bean.getIntroduction());
        courseRepository.save(one);
    }

    private List<CourseRegisterBean> queryCourseByCategory(String category, boolean after) {
        Date date = new Date();
        System.out.println("get course by category=" + category + ", date=" + date + ", after=" + after);
        List<CourseEntity> list = null;
        if (after) {
            list = courseRepository.findByCategoryAndDateAfter(category, date);
            list.addAll(courseRepository.findByCategoryAndDate(category, date));
        } else {
            list = courseRepository.findByCategoryAndDateBefore(category, date);
        }
        List<CourseRegisterBean> beanList = new ArrayList<>();
        for (CourseEntity entity : list) {
            CourseRegisterBean bean = new CourseRegisterBean(entity, wsUtility);
            beanList.add(bean);
        }
        return beanList;
    }

}
