package com.education.service;

import com.education.db.entity.CourseTypeEntity;
import com.education.db.jpa.CourseTypeRepository;
import com.education.ws.CourseRegisterBean;
import com.education.ws.CourseTypeService;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by yzzhao on 12/5/15.
 */
@Transactional
public class CourseServiceTest extends AbstractServiceTest {

    @Autowired
    private CourseService courseService;

    @Autowired
    private CourseTypeRepository courseTypeRepository;

    @Test
    public void testCreateCourse() {
        int courseTypeId = courseTypeRepository.findAll().iterator().next().getId();
        CourseRegisterBean bean = new CourseRegisterBean();
        bean.setCategory(String.valueOf(courseTypeId));
        String name = System.currentTimeMillis() + "";
        bean.setName(name);
        bean.setTitleImagePath("aaa");
        bean.setWeeks(13);
        bean.setContent("test");
        courseService.createCourse(bean);
        List<CourseRegisterBean> allCoursesIndex = courseService.getAllCoursesIndex();
        bean = null;
        for(CourseRegisterBean b: allCoursesIndex){
            if(b.getName().equals(name)){
                bean = b;
                break;
            }
        }
        Assert.assertNotNull(bean);
    }

}
