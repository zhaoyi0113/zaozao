package com.education.service;

import com.education.db.entity.CourseTagEntity;
import com.education.db.jpa.CourseTagRepository;
import com.education.exception.BadRequestException;
import com.education.exception.ErrorCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by yzzhao on 12/24/15.
 */
@Service("CourseTagService")
public class CourseTagService {

    @Autowired
    private CourseTagRepository courseTagRepository;

    @Transactional
    public int addNewCourseTag(String tagName) {
        List<CourseTagEntity> courseTagList = courseTagRepository.findCourseTagByName(tagName);
        if (courseTagList != null && !courseTagList.isEmpty()) {
            throw new BadRequestException(ErrorCode.COURSE_TAG_ALREADY_EXISTED);
        }
        CourseTagEntity entity = new CourseTagEntity();
        entity.setName(tagName);
        CourseTagEntity save = courseTagRepository.save(entity);
        return save.getId();
    }

    public Iterable<CourseTagEntity> getCourseTags(){
        return courseTagRepository.findAll();
    }
}
