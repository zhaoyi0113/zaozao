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

    public List<CourseRegisterBean> queryCourseByCategoryAfterNow(String category){
        return queryCourseByCategory(category, true);
    }

    public List<CourseRegisterBean> queryCourseByCategoryBeforeNow(String category){
        return queryCourseByCategory(category, false);
    }

    private List<CourseRegisterBean> queryCourseByCategory(String category, boolean after){
        Date date = new Date();
        System.out.println("get course by category="+category+", date="+date+", after="+after);
        List<CourseEntity> list = null;
        if(after){
            list = courseRepository.findByCategoryAndDateAfter(category, date);
            list.addAll(courseRepository.findByCategoryAndDate(category,date));
        }else {
            list= courseRepository.findByCategoryAndDateBefore(category, date);
        }
        List<CourseRegisterBean> beanList = new ArrayList<>();
        for(CourseEntity entity : list){
            CourseRegisterBean bean = new CourseRegisterBean(entity, wsUtility);
            beanList.add(bean);
        }
        return beanList;
    }

}
