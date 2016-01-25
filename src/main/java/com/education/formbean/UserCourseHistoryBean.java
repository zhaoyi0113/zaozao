package com.education.formbean;

import com.education.db.entity.COURSE_ACCESS_FLAG;

import java.util.Date;

/**
 * Created by yzzhao on 1/24/16.
 */
public class UserCourseHistoryBean {
    private long id;
    private int userId;
    private int courseId;
    private Date timeCreated;
    private COURSE_ACCESS_FLAG accessFlag;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public Date getTimeCreated() {
        return timeCreated;
    }

    public void setTimeCreated(Date timeCreated) {
        this.timeCreated = timeCreated;
    }

    public COURSE_ACCESS_FLAG getAccessFlag() {
        return accessFlag;
    }

    public void setAccessFlag(COURSE_ACCESS_FLAG accessFlag) {
        this.accessFlag = accessFlag;
    }
}
