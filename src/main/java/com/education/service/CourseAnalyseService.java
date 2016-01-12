package com.education.service;

import com.education.db.jpa.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Hashtable;
import java.util.List;
import java.util.Map;

/**
 * Created by yzzhao on 1/12/16.
 */
@Service
public class CourseAnalyseService {

    @Autowired
    private CourseRepository courseRepository;

    public Map<Integer, Long> getCoursePreviewCount() {
        List<Object[]> courseCount = courseRepository.findCourseCount();
        Map<Integer, Long> courses = getIntegerCountMap(courseCount);
        return courses;
    }

    public Map<Integer, Long> getCoursePreviewCountByUser(int userId) {
        List<Object[]> courseCount = courseRepository.findCourseCountByUser(userId);
        Map<Integer, Long> courses = getIntegerCountMap(courseCount);
        return courses;
    }

    private Map<Integer, Long> getIntegerCountMap(List<Object[]> courseCount) {
        Map<Integer, Long> courses = new Hashtable<Integer, Long>();
        for (Object[] objs : courseCount) {
            courses.put((Integer) objs[0], (Long) objs[1]);
        }
        return courses;
    }
}
