package com.education.service;

import com.education.db.entity.CourseTypeEntity;
import com.education.db.jpa.CourseTypeRepository;
import com.education.ws.CourseRegisterBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yzzhao on 12/15/15.
 */
@Service("CourseProposalService")
public class CourseProposalService {

    @Autowired
    private CourseTypeRepository courseTypeRepository;

    public List<CourseRegisterBean> proposeCourse(WeChatUserInfo userInfo) {
        List<CourseRegisterBean> courseList = new ArrayList<>();
        Iterable<CourseTypeEntity> courseTypes = courseTypeRepository.findAll();

        return courseList;
    }

}
