package com.education.db.entity;

import com.education.ws.CourseRegisterBean;

import javax.persistence.*;

/**
 * Created by yzzhao on 11/3/15.
 */
@Entity
@Table(name = "course")
public class CourseEntity {

    private int id;
    private String name;
    private String content;
    private String picture_paths;
    private String category;
    private String date;

    public CourseEntity(){

    }

    public CourseEntity(CourseRegisterBean bean){
        name = bean.getName();
        content = bean.getContent();
        picture_paths = bean.getPicturePaths();
        category = bean.getCategory();
        date = bean.getDate();
    }

    @Id
    @GeneratedValue
    @Column(name = "id")
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Column(name = "name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "content")
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Column(name = "picture_paths")
    public String getPicture_paths() {
        return picture_paths;
    }

    public void setPicture_paths(String picture_paths) {
        this.picture_paths = picture_paths;
    }

    @Column(name="category")
    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }


    @Column(name = "date")
    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
