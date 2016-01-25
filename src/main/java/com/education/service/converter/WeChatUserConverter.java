package com.education.service.converter;

import com.education.db.entity.UserEntity;
import com.education.service.WeChatUserInfo;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

/**
 * Created by yzzhao on 1/15/16.
 */
@Component
public class WeChatUserConverter implements Converter<UserEntity, WeChatUserInfo> {


    @Override
    public WeChatUserInfo convert(UserEntity userEntity) {
        WeChatUserInfo userInfo = new WeChatUserInfo();
        userInfo.setUserId(userEntity.getUserId());
        userInfo.setGroupid(userEntity.getGroupid());
        userInfo.setRemark(userEntity.getRemark());
        userInfo.setCity(userEntity.getCity());
        userInfo.setCountry(userEntity.getCountry());
        userInfo.setLanguage(userEntity.getLanguage());
        userInfo.setHeadimgurl(userEntity.getHeadimageurl());
        userInfo.setOpenid(userEntity.getOpenid());
        userInfo.setSex(userEntity.getSex());
        userInfo.setNickname(userEntity.getUserName());
        userInfo.setProvince(userEntity.getProvince());
        userInfo.setSubscribe(userEntity.getSubscribe());
        userInfo.setUnionid(userEntity.getUnionid());
        userInfo.setSubscribe_time(userEntity.getSubscribe_time());
        return userInfo;
    }


}
