package com.education.db.entity;

import javax.persistence.*;

/**
 * Created by yzzhao on 1/27/16.
 */
@Table(name = "user_profile_privilege")
@Entity
public class UserProfilePrivilegeEntity {

    private int id;
    private int userName;
    private int userImage;
    private int childName;
    private int childBirthdate;

    @Id
    @GeneratedValue
    @Column(name = "id")
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Column(name = "user_name")
    public int getUserName() {
        return userName;
    }

    public void setUserName(int userName) {
        this.userName = userName;
    }

    @Column(name = "user_image")
    public int getUserImage() {
        return userImage;
    }

    public void setUserImage(int userImage) {
        this.userImage = userImage;
    }

    @Column(name = "child_name")
    public int getChildName() {
        return childName;
    }

    public void setChildName(int childName) {
        this.childName = childName;
    }

    @Column(name = "child_birthdate")
    public int getChildBirthdate() {
        return childBirthdate;
    }

    public void setChildBirthdate(int childBirthdate) {
        this.childBirthdate = childBirthdate;
    }
}
