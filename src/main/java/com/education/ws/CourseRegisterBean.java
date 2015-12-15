package com.education.ws;

import com.education.db.entity.CourseEntity;
import com.education.ws.util.WSUtility;

import javax.ws.rs.FormParam;

/**
 * Created by yzzhao on 11/3/15.
 */
public class CourseRegisterBean {
    @FormParam("id")
    private String id;

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

    private String titleImagePath;

    private String videoPath;

    @FormParam("introduction")
    private String introduction;

    private String titleImageUrl;

    private String videoUrl;

    @FormParam("months")
    private int months;

    @FormParam("tags")
    private String tags;

    public CourseRegisterBean(){}

    public CourseRegisterBean(CourseEntity entity, WSUtility wsUtility){
        this.name = entity.getName();
        this.category = entity.getCategory();
//        this.date = wsUtility.dateToString(entity.getDate());
        this.content = entity.getContent();
        this.titleImagePath = entity.getTitleImagePath();
        this.videoPath = entity.getVideoPath();

        if(entity.getTitleImagePath() != null) {
            this.titleImageUrl = wsUtility.getResourcePath(entity.getTitleImagePath());
        }
        if(entity.getVideoPath() != null) {
            this.videoUrl = wsUtility.getResourcePath(entity.getVideoPath());
        }
        this.introduction = entity.getIntroduction();
        this.id = String.valueOf(entity.getId());
        this.tags = entity.getTags();

        this.months = entity.getMonths();
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    /**
     * Get the course name
     * @return
     */
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitleImagePath() {
        return titleImagePath;
    }

    public void setTitleImagePath(String titleImagePath) {
        this.titleImagePath = titleImagePath;
    }

    public String getVideoPath() {
        return videoPath;
    }

    public void setVideoPath(String videoPath) {
        this.videoPath = videoPath;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public String getTitleImageUrl() {
        return titleImageUrl;
    }

    public void setTitleImageUrl(String titleImageUrl) {
        this.titleImageUrl = titleImageUrl;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public int getMonths() {
        return months;
    }

    public void setMonths(int months) {
        this.months = months;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    @Override
    public String toString() {
        StringBuffer buffer =new StringBuffer();
        buffer.append("id="+id+",name="+name+",content="+content+",category="+category+",pictures="+picturePaths+",date"+date);
        return buffer.toString();
    }
}
