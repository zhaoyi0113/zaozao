package com.education.service;

import com.education.db.entity.CourseEntity;
import com.education.db.entity.HomeCourseEntity;
import com.education.db.jpa.CourseRepository;
import com.education.db.jpa.HomeCourseRepository;
import com.education.exception.BadRequestException;
import com.education.exception.ErrorCode;
import com.education.formbean.HomeCourseBean;
import com.education.service.converter.HomeCourseBeanConverter;
import com.education.util.MoveAction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yzzhao on 1/20/16.
 */
@Service("HomeCourseService")
public class HomeCourseService {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private HomeCourseRepository homeCourseRepository;

    @Autowired
    private HomeCourseBeanConverter converter;

    @Transactional
    public int addCourseOnHomePage(int courseId) {
        CourseEntity course = courseRepository.findOne(courseId);
        if (course == null) {
            throw new BadRequestException(ErrorCode.COURSE_NOT_FOUND);
        }
        List<HomeCourseEntity> existedCourses = homeCourseRepository.findByCourseId(courseId);
        if (existedCourses != null && !existedCourses.isEmpty()) {
            throw new BadRequestException(ErrorCode.COURSE_ALREADY_EXISTED);
        }
        HomeCourseEntity topCourse = homeCourseRepository.findTopByOrderByOrderIndexDesc();
        int order = 0;
        if (topCourse != null) {
            order = topCourse.getOrderIndex() + 1;
        }
        HomeCourseEntity homeCourseEntity = new HomeCourseEntity();
        homeCourseEntity.setCourseId(course.getId());
        homeCourseEntity.setOrderIndex(order);
        HomeCourseEntity saved = homeCourseRepository.save(homeCourseEntity);
        return saved.getId();
    }

    @Transactional
    private void moveUp(int id) {
        HomeCourseEntity config = homeCourseRepository.findOne(id);
        if (config == null) {
            throw new BadRequestException(ErrorCode.COMMON_NOT_FOUND);
        }
        List<HomeCourseEntity> configBefore = homeCourseRepository.findByOrderIndexLessThanOrderByOrderIndex(config.getOrderIndex());
        if (configBefore.size() > 0) {
            HomeCourseEntity last = configBefore.get(configBefore.size() - 1);
            int order = config.getOrderIndex();
            config.setOrderIndex(last.getOrderIndex());
            last.setOrderIndex(order);
            homeCourseRepository.save(last);
            homeCourseRepository.save(config);
        }
    }

    @Transactional
    private void moveDown(int id) {
        HomeCourseEntity config = homeCourseRepository.findOne(id);
        if (config == null) {
            throw new BadRequestException(ErrorCode.COMMON_NOT_FOUND);
        }
        List<HomeCourseEntity> items = homeCourseRepository.findByOrderIndexGreaterThanOrderByOrderIndex(config.getOrderIndex());
        if (items.size() > 0) {
            HomeCourseEntity item = items.get(0);
            int order = item.getOrderIndex();
            item.setOrderIndex(config.getOrderIndex());
            config.setOrderIndex(order);
            homeCourseRepository.save(item);
            homeCourseRepository.save(config);
        }
    }

    public List<HomeCourseBean> getHomeCourses() {
        Iterable<HomeCourseEntity> entities = homeCourseRepository.findOrderByOrderIndex();
        List<HomeCourseBean> beans = new ArrayList<>();
        for (HomeCourseEntity entity : entities) {
            HomeCourseBean bean = converter.convert(entity);
            beans.add(bean);
        }
        return beans;
    }


    public void moveAction(int id, String action) {
        MoveAction moveAction = MoveAction.valueOf(action);
        switch (moveAction) {
            case UP:
                moveUp(id);
                break;
            case DOWN:
                moveDown(id);
                break;
        }
    }

    @Transactional
    public void deleteCourse(int id) {
        HomeCourseEntity course = homeCourseRepository.findOne(id);
        if (course == null) {
            throw new BadRequestException(ErrorCode.COURSE_NOT_FOUND);
        }
        homeCourseRepository.delete(id);
    }
}
