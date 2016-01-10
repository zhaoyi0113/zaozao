package com.education.db.entity;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by yzzhao on 1/9/16.
 */
@Table(name = "user_course_history")
@Entity
public class UserCourseHistoryEntity {
    private long id;
    private int userId;
    private int courseId;
    private Date timeCreated;
    private COURSE_ACCESS_FLAG accessFlag;

    @Id
    @GeneratedValue
    @Column(name = "id")
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Column(name = "user_id")
    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    @Column(name = "course_id")
    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    @Column(name = "time_created")
    public Date getTimeCreated() {
        return timeCreated;
    }

    public void setTimeCreated(Date timeCreated) {
        this.timeCreated = timeCreated;
    }

    @Column(name = "access_flag")
    @Enumerated
    public COURSE_ACCESS_FLAG getAccessFlag() {
        return accessFlag;
    }

    public void setAccessFlag(COURSE_ACCESS_FLAG accessFlag) {
        this.accessFlag = accessFlag;
    }
}
