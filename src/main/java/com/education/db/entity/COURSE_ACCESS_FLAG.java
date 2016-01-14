package com.education.db.entity;

/**
 * Created by yzzhao on 1/9/16.
 */
public enum COURSE_ACCESS_FLAG {
    VIEW("VIEW"), SHARE("SHARE"), FAVORITE("FAVORITE"), GUEST("GUEST");

    private String name;
    COURSE_ACCESS_FLAG(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

}
