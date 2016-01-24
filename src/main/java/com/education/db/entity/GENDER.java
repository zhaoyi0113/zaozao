package com.education.db.entity;

/**
 * Created by yzzhao on 1/24/16.
 */
public enum GENDER {

    MALE("MALE"), FEMAIL("FEMALE");

    private String gender;

    GENDER(String gender) {
        this.gender = gender;
    }

    public String getValue(){
        return gender;
    }
}
