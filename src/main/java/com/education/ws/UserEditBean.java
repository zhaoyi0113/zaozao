package com.education.ws;

import javax.ws.rs.FormParam;

/**
 * Created by yzzhao on 11/1/15.
 */
public class UserEditBean {

    @FormParam("user_id")
    private int userId;

    @FormParam("user_name")
    private String userName;

    @FormParam("phone")
    private String phone;

    @FormParam("birthdate")
    private String birthdate;

    @FormParam("gender")
    private String gender;

    @FormParam("age")
    private int age;

    @FormParam("email")
    private String email;


    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(String birthdate) {
        this.birthdate = birthdate;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("user_id="+userId+";user_name="+userName+";age="+age+";gender="+gender+";email="+email+";phone="+phone+";birthdate="+birthdate);
        return buffer.toString();
    }
}
