package com.education.service.converter;

import com.education.db.entity.UserProfilePrivilegeEntity;
import com.education.formbean.UserProfilePrivilegeBean;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

/**
 * Created by yzzhao on 1/27/16.
 */
@Component
public class UserProfilePrivilegeBeanConverter implements Converter<UserProfilePrivilegeEntity, UserProfilePrivilegeBean> {
    @Override
    public UserProfilePrivilegeBean convert(UserProfilePrivilegeEntity source) {
        UserProfilePrivilegeBean bean = new UserProfilePrivilegeBean();
        bean.setChildBirthdate(source.getChildBirthdate());
        bean.setChildName(source.getChildName());
        bean.setId(source.getId());
        bean.setUserImage(source.getUserImage());
        bean.setUserName(source.getUserName());
        return bean;
    }
}
