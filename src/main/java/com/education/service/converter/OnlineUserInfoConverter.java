package com.education.service.converter;

import com.education.service.OnlineUserInfo;
import com.education.service.WeChatUserInfo;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

/**
 * Created by yzzhao on 2/1/16.
 */
@Component
public class OnlineUserInfoConverter implements Converter<WeChatUserInfo, OnlineUserInfo> {

    @Override
    public OnlineUserInfo convert(WeChatUserInfo source) {
        OnlineUserInfo userInfo = new OnlineUserInfo();
        userInfo.setUserId(source.getUserId());
        userInfo.setSubscribe(source.getSubscribe());
        userInfo.setOpenid(source.getOpenid());
        userInfo.setNickname(source.getNickname());
        userInfo.setCity(source.getCity());
        userInfo.setSex(source.getSex());
        userInfo.setLanguage(source.getLanguage());
        userInfo.setProvince(source.getProvince());
        userInfo.setCountry(source.getCountry());
        userInfo.setHeadimgurl(source.getHeadimgurl());
        userInfo.setSubscribe_time(source.getSubscribe_time());
        userInfo.setUnionid(source.getUnionid());
        userInfo.setRemark(source.getRemark());
        userInfo.setGroupid(source.getGroupid());
        userInfo.setErrcode(source.getErrcode());
        userInfo.setErrmsg(source.getErrmsg());
        userInfo.setChild(source.getChild());
        userInfo.setPrivilege(source.getPrivilege());
        return userInfo;
    }
}
