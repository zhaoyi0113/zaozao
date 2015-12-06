package com.education.service;

import com.education.db.entity.CourseEntity;
import com.education.db.jpa.CourseRepository;
import com.education.ws.CourseRegisterBean;
import com.education.ws.util.WSUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by yzzhao on 12/5/15.
 */
@Service("CourseService")
public class CourseService {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private WSUtility wsUtility;

    public List<CourseRegisterBean> queryCourseByCategory(String category){
        System.out.println("get course by category="+category);
        Date date = new Date();
        List<CourseEntity> list = courseRepository.findByCategoryAndDateAfter(category, date);
        List<CourseRegisterBean> beanList = new ArrayList<>();
        for(CourseEntity entity : list){
            CourseRegisterBean bean = new CourseRegisterBean(entity, wsUtility);
            beanList.add(bean);
        }
        return beanList;
    }


}
