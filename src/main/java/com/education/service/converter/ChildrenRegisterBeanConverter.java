package com.education.service.converter;

import com.education.db.entity.ChildrenEntity;
import com.education.formbean.UserChildrenRegisterBean;
import com.education.ws.util.WSUtility;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

/**
 * Created by yzzhao on 1/25/16.
 */
@Component
public class ChildrenRegisterBeanConverter implements Converter<ChildrenEntity, UserChildrenRegisterBean> {

    @Override
    public UserChildrenRegisterBean convert(ChildrenEntity source) {
        UserChildrenRegisterBean bean = new UserChildrenRegisterBean();
        bean.setId(source.getId());
        bean.setChildBirthdate(WSUtility.dateToString(source.getBirthdate()));
        bean.setChildName(source.getName());
        bean.setGender(source.getGender());
        bean.setAge(source.getAge());
        return bean;
    }
}
