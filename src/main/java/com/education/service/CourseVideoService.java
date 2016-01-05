package com.education.service;

import com.education.db.entity.CourseEntity;
import com.education.db.jpa.CourseRepository;
import com.education.exception.BadRequestException;
import com.education.exception.ErrorCode;
import com.education.ws.util.WSUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.InputStream;

/**
 * Created by yzzhao on 1/5/16.
 */
@Service("courseVideoService")
public class CourseVideoService {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private WSUtility wsUtility;

    public String getVideoUrl(int courseId) {
        CourseEntity course = courseRepository.findOne(courseId);
        if (course != null) {
            if(course.getVideoPath() == null){
                return null;
            }
            return wsUtility.getResourcePath(course.getVideoPath());
        }
        throw new BadRequestException(ErrorCode.COURSE_NOT_FOUND);
    }

    @Transactional
    public void setVideo(int courseId, String fileName, InputStream inputStream) {
        CourseEntity course = courseRepository.findOne(courseId);
        if (course == null) {
            throw new BadRequestException(ErrorCode.COURSE_NOT_FOUND);
        }
        String videoPath = course.getVideoPath();
        wsUtility.deleteCourseFile(videoPath);
        course.setVideoPath(fileName);
        wsUtility.writeCourseFile(fileName, inputStream);
        courseRepository.save(course);
    }

}
