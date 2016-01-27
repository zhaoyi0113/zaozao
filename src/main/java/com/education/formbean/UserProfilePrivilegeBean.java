package com.education.formbean;

import javax.ws.rs.FormParam;

/**
 * Created by yzzhao on 1/27/16.
 */
public class UserProfilePrivilegeBean {
    @FormParam("id")
    private int id;

    @FormParam("user_name")
    private int userName;

    @FormParam("user_image")
    private int userImage;

    @FormParam("child_name")
    private int childName;

    @FormParam("child_birthdate")
    private int childBirthdate;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserName() {
        return userName;
    }

    public void setUserName(int userName) {
        this.userName = userName;
    }

    public int getUserImage() {
        return userImage;
    }

    public void setUserImage(int userImage) {
        this.userImage = userImage;
    }

    public int getChildName() {
        return childName;
    }

    public void setChildName(int childName) {
        this.childName = childName;
    }

    public int getChildBirthdate() {
        return childBirthdate;
    }

    public void setChildBirthdate(int childBirthdate) {
        this.childBirthdate = childBirthdate;
    }
}
