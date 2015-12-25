package com.education.service;

import com.education.db.entity.CourseTagEntity;
import com.education.db.jpa.CourseTagRelationRepository;
import com.education.db.jpa.CourseTagRepository;
import com.education.exception.BadRequestException;
import com.education.exception.ErrorCode;
import com.education.formbean.CourseTagBean;
import com.education.ws.util.WSUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by yzzhao on 12/24/15.
 */
@Service("CourseTagService")
public class CourseTagService {

    @Value("#{config['course_tag_image_path']}")
    private String courseTagImagePath;

    @Value("#{config['course_tag_image_url']}")
    private String courseTagImageUrl;

    @Autowired
    private CourseTagRepository courseTagRepository;

    @Autowired
    private WSUtility wsUtility;

    @Autowired
    private CourseTagRelationRepository courseTagRelationRepository;

    @Transactional
    public int addNewCourseTag(String tagName, String imageName, InputStream inputStream) {
        List<CourseTagEntity> courseTagList = courseTagRepository.findCourseTagByName(tagName);
        if (courseTagList != null && !courseTagList.isEmpty()) {
            throw new BadRequestException(ErrorCode.COURSE_TAG_ALREADY_EXISTED);
        }
        CourseTagEntity entity = new CourseTagEntity();
        entity.setName(tagName);
        entity.setImage(imageName);
        CourseTagEntity save = courseTagRepository.save(entity);
        WSUtility.writeFile(inputStream, courseTagImagePath, imageName);
        return save.getId();
    }

    @Transactional
    public void editCourseTag(int id, String tagName, String imageName, InputStream inputStream) {
        CourseTagEntity courseTag = courseTagRepository.findOne(id);
        if (courseTag == null) {
            throw new BadRequestException(ErrorCode.COURSE_TAG_NOT_EXIST);
        }
        if(imageName != null) {
            courseTag.setImage(imageName);
        }
        courseTag.setName(tagName);
        if (inputStream != null) {
            WSUtility.writeFile(inputStream, courseTagImagePath, imageName);
        }
        courseTagRepository.save(courseTag);
    }



    public List<CourseTagBean> getCourseTags() {
        Iterable<CourseTagEntity> all = courseTagRepository.findAll();
        List<CourseTagBean> allTags = new ArrayList<>();
        for (Iterator<CourseTagEntity> iterator = all.iterator(); iterator.hasNext(); ) {
            CourseTagEntity next = iterator.next();
            CourseTagBean courseTagBean = createCourseTagBean(next);
            allTags.add(courseTagBean);
        }
        return allTags;
    }

    public CourseTagBean getCourseTag(int id) {
        CourseTagEntity courseTag = courseTagRepository.findOne(id);
        if (courseTag == null) {
            throw new BadRequestException(ErrorCode.COURSE_TAG_NOT_EXIST);
        }
        return createCourseTagBean(courseTag);
    }

    @Transactional
    public void deleteCourseTag(int courseTagId) {
        CourseTagEntity courseTag = courseTagRepository.findOne(courseTagId);
        if (courseTag == null) {
            throw new BadRequestException(ErrorCode.COURSE_TAG_NOT_EXIST);
        }
        courseTagRepository.delete(courseTag);
        courseTagRelationRepository.removeByCourseTagId(courseTagId);
    }

    private CourseTagBean createCourseTagBean(CourseTagEntity courseTag) {
        CourseTagBean bean = new CourseTagBean(courseTag.getId(), courseTag.getName());
        bean.setImageUrl(courseTagImageUrl + "/" + courseTag.getImage());
        return bean;
    }
}
