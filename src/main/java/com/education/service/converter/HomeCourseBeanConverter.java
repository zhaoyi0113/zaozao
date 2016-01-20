package com.education.service.converter;

import com.education.db.entity.HomeCourseEntity;
import com.education.formbean.HomeCourseBean;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

/**
 * Created by yzzhao on 1/20/16.
 */
@Component
public class HomeCourseBeanConverter implements Converter<HomeCourseEntity, HomeCourseBean> {

    @Override
    public HomeCourseBean convert(HomeCourseEntity source) {
        HomeCourseBean bean = new HomeCourseBean();
        bean.setId(source.getId());
        bean.setCourseId(source.getCourseId());
        bean.setOrderIndex(source.getOrderIndex());
        return bean;
    }
}
