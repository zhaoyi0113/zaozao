package com.education.formbean;

/**
 * Created by yzzhao on 12/25/15.
 */
public class HomeConfigResp {
    private int id;
    private String image;
    private String fileName;
    private int courseId;

    public HomeConfigResp() {
    }

    public HomeConfigResp(int id, String image, String fileName, int courseId) {
        this.id = id;
        this.image = image;
        this.fileName= fileName;
        this.courseId = courseId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }
}
