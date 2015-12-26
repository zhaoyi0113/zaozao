package com.education.db.entity;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by yzzhao on 12/26/15.
 */
@Entity
@Table(name = "backend_login_history")
public class BackendLoginHistoryEntity {
    private int id;
    private int userId;
    private Date loginTime;
    private int loginStatus;

    @GeneratedValue
    @Id
    @Column(name = "id")
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

    @Column(name = "login_time")
    public Date getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(Date loginTime) {
        this.loginTime = loginTime;
    }

    @Column(name = "login_status")
    public int getLoginStatus() {
        return loginStatus;
    }

    public void setLoginStatus(int loginStatus) {
        this.loginStatus = loginStatus;
    }
}
