package com.education.formbean;

import com.education.db.entity.COURSE_ACCESS_FLAG;
import com.education.service.WeChatUserInfo;

import java.util.Date;

/**
 * Created by yzzhao on 1/16/16.
 */
public class CourseUserAnalyticsBean {
    private WeChatUserInfo userInfo;

    private COURSE_ACCESS_FLAG flag;

    private Date accessDate;

    public WeChatUserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(WeChatUserInfo userInfo) {
        this.userInfo = userInfo;
    }

    public COURSE_ACCESS_FLAG getFlag() {
        return flag;
    }

    public void setFlag(COURSE_ACCESS_FLAG flag) {
        this.flag = flag;
    }

    public Date getAccessDate() {
        return accessDate;
    }

    public void setAccessDate(Date accessDate) {
        this.accessDate = accessDate;
    }
}
