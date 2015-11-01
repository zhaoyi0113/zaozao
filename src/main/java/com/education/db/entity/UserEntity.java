package com.education.db.entity;

import com.education.ws.UserRegisterBean;

import javax.persistence.*;

/**
 * Created by yzzhao on 11/1/15.
 */
@Entity
@Table(name = "user")
public class UserEntity {

    private int userId;
    private String userName;
    private String password;
    private String gender;
    private int age;

    public UserEntity() {
    }

    public UserEntity(UserRegisterBean u){
        userName = u.getUserName();
        password = u.getPassword();
        gender = u.getGender();
        age = u.getAge();
    }

    @Id
    @GeneratedValue
    @Column(name = "user_id")
    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    @Column(name = "user_name")
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Column(name = "password")
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Column(name = "gender")
    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    @Column(name = "age")
    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

}
