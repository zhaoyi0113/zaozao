package com.education.ws;

import javax.ws.rs.FormParam;

/**
 * Created by yzzhao on 11/3/15.
 */
public class CourseRegisterBean {
    @FormParam("name")
    private String name;

    @FormParam("content")
    private String content;

    @FormParam("category")
    private String category;

    @FormParam("picture_paths")
    private String picturePaths;

    @FormParam("date")
    private String date;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getPicturePaths() {
        return picturePaths;
    }

    public void setPicturePaths(String picturePaths) {
        this.picturePaths = picturePaths;
    }


    @Override
    public String toString() {
        StringBuffer buffer =new StringBuffer();
        buffer.append("name="+name+",content="+content+",category="+category+",pictures="+picturePaths);
        return buffer.toString();
    }
}
