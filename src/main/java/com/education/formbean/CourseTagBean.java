package com.education.formbean;

/**
 * Created by yzzhao on 12/24/15.
 */
public class CourseTagBean {

    private int id;
    private String name;
    private String imageUrl;

    public CourseTagBean(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
