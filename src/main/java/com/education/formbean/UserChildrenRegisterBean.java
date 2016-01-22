package com.education.formbean;

import javax.ws.rs.FormParam;

/**
 * Created by yzzhao on 10/31/15.
 */
public class UserChildrenRegisterBean {

    @FormParam("id")
    private int id;

    @FormParam("gender")
    private String gender;

    @FormParam("child_birthdate")
    private String childBirthdate;

    @FormParam("child_name")
    private String childName;

    @FormParam("age")
    private int age;

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
