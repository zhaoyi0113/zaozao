package com.education.service.mock;

import com.education.db.entity.CommonStatus;
import com.education.db.entity.CourseTagEntity;
import com.education.db.entity.CourseTagRelationEntity;
import com.education.db.jpa.CourseTagRelationRepository;
import com.education.db.jpa.CourseTagRepository;
import com.education.jerseytest.AbstractJerseyTest;
import com.education.service.AbstractServiceTest;
import com.education.service.CourseService;
import com.education.ws.CourseRegisterBean;
import com.education.ws.util.WSUtility;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Calendar;

/**
 * Created by yzzhao on 1/7/16.
 */
@Ignore
public class MockDataCreator extends AbstractServiceTest {

    private static int index = 1;

    @Autowired
    private CourseService courseService;

    @Autowired
    private CourseTagRelationRepository tagRelationRepository;

    @Autowired
    private CourseTagRepository tagRepository;

    @Test
    public void createCourseData() {

        Iterable<CourseTagEntity> couresTags = tagRepository.findAll();
        String tags = "";
        for(CourseTagEntity entity : couresTags){
            tags += entity.getId()+",";
        }
        for (int i = 0; i < 100; i++) {
            CourseRegisterBean course = createCourse();
            course.setTags(tags);
            int courseId = courseService.createCourse(course);


        }
    }

    private CourseRegisterBean createCourse() {
        CourseRegisterBean bean = new CourseRegisterBean();
        bean.setName(index + "");
        bean.setContent(index + "");
        bean.setIntroduction(index + "");
        bean.setTitleImagePath("1.png");
        index++;
        bean.setPublishDate(WSUtility.dateToString(Calendar.getInstance().getTime()));
        bean.setStatus(CommonStatus.ENABLED.name());
        return bean;
    }
}
