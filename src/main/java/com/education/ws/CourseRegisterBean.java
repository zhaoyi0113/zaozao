package com.education.ws;

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

    @FormParam("years")
    private int years;

    @FormParam("tags")
    private String tags;

    @FormParam("video_external_url")
    private String videoExternalUrl;

    @FormParam("day_number")
    private int dayNumber;

    @FormParam("status")
    private String status;

    @FormParam("publish_date")
    private String publishDate;

    @FormParam("video_length")
    private double videoLength;

    @FormParam("pv")
    private long pv;

    public CourseRegisterBean() {
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    /**
     * Get the course name
     *
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

    public int getYears() {
        return years;
    }

    public void setYears(int years) {
        this.years = years;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getVideoExternalUrl() {
        return videoExternalUrl;
    }

    public void setVideoExternalUrl(String videoExternalUrl) {
        this.videoExternalUrl = videoExternalUrl;
    }

    public int getDayNumber() {
        return dayNumber;
    }

    public void setDayNumber(int dayNumber) {
        this.dayNumber = dayNumber;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(String publishDate) {
        this.publishDate = publishDate;
    }

    public double getVideoLength() {
        return videoLength;
    }

    public void setVideoLength(double videoLength) {
        this.videoLength = videoLength;
    }

    public long getPv() {
        return pv;
    }

    public void setPv(long pv) {
        this.pv = pv;
    }

    @Override
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("id=" + id + ",name=" + name + ",content=" + content + ",category=" + category + ",pictures=" + picturePaths + ",date" + date);
        return buffer.toString();
    }
}
