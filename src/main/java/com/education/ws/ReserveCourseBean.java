package com.education.ws;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.ws.rs.FormParam;
import java.util.Date;

/**
 * Created by yzzhao on 11/12/15.
 */
public class ReserveCourseBean {

    @FormParam("mobile")
    private String mobile;

    @FormParam("name")
    private String name;

    @FormParam("date")
    private String date;

    @FormParam("child_birthdate")
    private String childBirthdate;

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getChildBirthdate() {
        return childBirthdate;
    }

    public void setChildBirthdate(String childBirthdate) {
        this.childBirthdate = childBirthdate;
    }
}
