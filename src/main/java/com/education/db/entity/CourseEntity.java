package com.education.db.entity;

import com.education.ws.CourseRegisterBean;
import com.education.ws.WSUtility;

import javax.persistence.*;
import java.text.SimpleDateFormat;
import java.util.Date;

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
    private Date date;
    private String videoPath;
    private String titleImagePath;

    public CourseEntity() {

    }

    public CourseEntity(CourseRegisterBean bean) {
        name = bean.getName();
        content = bean.getContent();
        picture_paths = bean.getPicturePaths();
        category = bean.getCategory();
        videoPath = bean.getVideoPath();
        titleImagePath = bean.getTitleImagePath();
        try{
            SimpleDateFormat format = WSUtility.getDateFormat();
             date = format.parse(bean.getDate());
            format.format(this.date);
        }catch (Exception e){
            e.printStackTrace();
        }
        if (bean.getId() != null) {
            id = Integer.parseInt(bean.getId());
        }
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

    @Column(name = "category")
    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }


    @Column(name = "date")
    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {

        this.date = date;
    }

    @Column(name="video_path")
    public String getVideoPath() {
        return videoPath;
    }

    public void setVideoPath(String videoPath) {
        this.videoPath = videoPath;
    }

    @Column(name="title_image_path")
    public String getTitleImagePath() {
        return titleImagePath;
    }

    public void setTitleImagePath(String titleImagePath) {
        this.titleImagePath = titleImagePath;
    }
}
