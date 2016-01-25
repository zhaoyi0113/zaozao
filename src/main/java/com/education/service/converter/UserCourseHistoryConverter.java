package com.education.service.converter;

import com.education.db.entity.UserCourseHistoryEntity;
import com.education.formbean.UserCourseHistoryBean;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

/**
 * Created by yzzhao on 1/24/16.
 */
@Component
public class UserCourseHistoryConverter implements Converter<UserCourseHistoryEntity, UserCourseHistoryBean> {
    @Override
    public UserCourseHistoryBean convert(UserCourseHistoryEntity source) {
        UserCourseHistoryBean bean = new UserCourseHistoryBean();
        bean.setUserId(source.getUserId());
        bean.setAccessFlag(source.getAccessFlag());
        bean.setCourseId(source.getCourseId());
        bean.setId(source.getId());
        bean.setTimeCreated(source.getTimeCreated());
        return bean;
    }
}
