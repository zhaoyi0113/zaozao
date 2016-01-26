package com.education.db.entity;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by yzzhao on 1/26/16.
 */
@Table(name = "backend_user_tracking")
@Entity
public class BackendUserTrackingEntity {
    private int id;
    private int userId;
    private String comments;
    private Date timeCreated;

    @Column(name = "id")
    @GeneratedValue
    @Id
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Column(name = "user_id")
    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    @Column(name = "comments")
    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    @Column(name="time_created")
    public Date getTimeCreated() {
        return timeCreated;
    }

    public void setTimeCreated(Date timeCreated) {
        this.timeCreated = timeCreated;
    }
}
