package com.education.service.converter;

import com.education.db.entity.BackendUserTrackingEntity;
import com.education.formbean.BackendUserTrackingBean;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

/**
 * Created by yzzhao on 1/26/16.
 */
@Component
public class BackendUserTrackingBeanConverter implements Converter<BackendUserTrackingEntity, BackendUserTrackingBean> {
    @Override
    public BackendUserTrackingBean convert(BackendUserTrackingEntity entity) {
        BackendUserTrackingBean bean = new BackendUserTrackingBean();
        bean.setId(entity.getId());
        bean.setTimeCreated(entity.getTimeCreated());
        bean.setUserId(entity.getUserId());
        bean.setComments(entity.getComments());
        return bean;
    }
}
