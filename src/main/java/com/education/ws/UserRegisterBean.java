package com.education.ws;

import javax.ws.rs.FormParam;

/**
 * Created by yzzhao on 10/31/15.
 */
public class UserRegisterBean {
    @FormParam("userName")
    private String userName;

    @FormParam("password")
    private String password;

    @FormParam("gender")
    private String gender;

    @FormParam("age")
    private int age;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @Override
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("userName=").append(userName).append("\nPassword=").append(password).append("\nGender=").append(gender).append("\nAge=").append(age);
        return buffer.toString();
    }

}
