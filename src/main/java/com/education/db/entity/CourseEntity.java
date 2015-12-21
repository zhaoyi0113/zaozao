package com.education.db.entity;

import com.education.ws.CourseRegisterBean;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by yzzhao on 11/3/15.
 */
@Entity
@Table(name = "course")
public class CourseEntity {

    private int id;
    private String name;
    private String introduction;
    private String content;
    private String picture_paths;
    private int category;
    private Date date;
    private String videoPath;
    private String titleImagePath;
    private String tags;
    private Integer years;
    private String videoExternalUrl;

    public CourseEntity() {

    }

    public CourseEntity(CourseRegisterBean bean) {
        name = bean.getName();
        content = bean.getContent();
        picture_paths = bean.getPicturePaths();
        category = Integer.parseInt(bean.getCategory());
        videoPath = bean.getVideoPath();
        titleImagePath = bean.getTitleImagePath();
//        date = WSUtility.stringToDate(bean.getDate());
        if (bean.getId() != null) {
            id = Integer.parseInt(bean.getId());
        }
        introduction = bean.getIntroduction();
        tags = bean.getTags();
        years = bean.getYears();
        videoExternalUrl = bean.getVideoExternalUrl();
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

    @Column(name = "category_id")
    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
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

    @Column(name="introduction")
    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    @Column(name="tags")
    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }
    @Column(name="years")
    public Integer getYears() {
        return years;
    }

    @Column(name="video_external_url")
    public String getVideoExternalUrl() {
        return videoExternalUrl;
    }

    public void setVideoExternalUrl(String videoExternalUrl) {
        this.videoExternalUrl = videoExternalUrl;
    }

    public void setYears(Integer years) {
        this.years = years;
    }
}
