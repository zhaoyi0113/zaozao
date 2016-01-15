package com.education.service.converter;

import com.education.db.entity.UserEntity;
import com.education.service.WeChatUserInfo;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

/**
 * Created by yzzhao on 1/15/16.
 */
@Component
public class UserEntityConverter implements Converter<WeChatUserInfo, UserEntity> {
    @Override
    public UserEntity convert(WeChatUserInfo userInfo) {
        UserEntity entity = new UserEntity();
        entity.setGroupid(userInfo.getGroupid());
        entity.setRemark(userInfo.getRemark());
        entity.setCity(userInfo.getCity());
        entity.setCountry(userInfo.getCountry());
        entity.setLanguage(userInfo.getLanguage());
        entity.setHeadimageurl(userInfo.getHeadimgurl());
        entity.setOpenid(userInfo.getOpenid());
        entity.setSex(userInfo.getSex());
        entity.setNickname(userInfo.getNickname());
        entity.setUserName(userInfo.getNickname());
        entity.setProvince(userInfo.getProvince());
        entity.setSubscribe(userInfo.getSubscribe());
        entity.setUnionid(userInfo.getUnionid());
        entity.setSubscribe_time(userInfo.getSubscribe_time());
        return entity;
    }
}
