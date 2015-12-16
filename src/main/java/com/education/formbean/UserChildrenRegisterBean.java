package com.education.formbean;

import javax.ws.rs.FormParam;

/**
 * Created by yzzhao on 10/31/15.
 */
public class UserChildrenRegisterBean {

    private int openid;

    @FormParam("gender")
    private String gender;

    @FormParam("phone")
    private String phone;

    @FormParam("child_birthdate")
    private String childBirthdate;

    @FormParam("child_name")
    private String childName;

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public int getOpenid() {
        return openid;
    }

    public void setOpenid(int openid) {
        this.openid = openid;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getChildBirthdate() {
        return childBirthdate;
    }

    public void setChildBirthdate(String childBirthdate) {
        this.childBirthdate = childBirthdate;
    }

    public String getChildName() {
        return childName;
    }

    public void setChildName(String childName) {
        this.childName = childName;
    }
}
