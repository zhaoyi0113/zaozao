package com.education.db.entity;

import com.education.ws.CourseRegisterBean;
import com.education.ws.util.WSUtility;

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
    private int years;
    private String videoExternalUrl;
    private int dayNumber;
    private Date timeCreated;
    private CommonStatus status;
    private Date publishDate;
    private double videoLength;

    public CourseEntity() {

    }

    public CourseEntity(CourseRegisterBean bean) {
        name = bean.getName();
        content = bean.getContent();
        picture_paths = bean.getPicturePaths();
        videoPath = bean.getVideoPath();
        titleImagePath = bean.getTitleImagePath();
        if (bean.getId() != null) {
            id = Integer.parseInt(bean.getId());
        }
        introduction = bean.getIntroduction();
        videoExternalUrl = bean.getVideoExternalUrl();
        dayNumber = bean.getDayNumber();
        publishDate = WSUtility.stringToDate(bean.getPublishDate());
        if (bean.getStatus() == null) {
            status = CommonStatus.ENABLED;
        } else {
            status = CommonStatus.valueOf(bean.getStatus());
        }
        videoLength = bean.getVideoLength();
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

    @Column(name = "video_path")
    public String getVideoPath() {
        return videoPath;
    }

    public void setVideoPath(String videoPath) {
        this.videoPath = videoPath;
    }

    @Column(name = "title_image_path")
    public String getTitleImagePath() {
        return titleImagePath;
    }

    public void setTitleImagePath(String titleImagePath) {

        this.titleImagePath = titleImagePath;
    }

    @Column(name = "introduction")
    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    @Column(name = "tags")
    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    @Column(name = "years")
    public int getYears() {
        return years;
    }

    @Column(name = "video_external_url")
    public String getVideoExternalUrl() {
        return videoExternalUrl;
    }

    public void setVideoExternalUrl(String videoExternalUrl) {
        this.videoExternalUrl = videoExternalUrl;
    }

    public void setYears(int years) {
        this.years = years;
    }

    @Column(name = "day_number")
    public int getDayNumber() {
        return dayNumber;
    }

    public void setDayNumber(int dayNumber) {
        this.dayNumber = dayNumber;
    }

    @Column(name = "time_created")
    public Date getTimeCreated() {
        return timeCreated;
    }

    public void setTimeCreated(Date timeCreated) {
        this.timeCreated = timeCreated;
    }

    @Column(name = "status")
    @Enumerated
    public CommonStatus getStatus() {
        return status;
    }

    public void setStatus(CommonStatus status) {
        this.status = status;
    }

    @Column(name = "publish_date")
    public Date getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(Date publishDate) {
        this.publishDate = publishDate;
    }

    @Column(name = "video_length")
    public double getVideoLength() {
        return videoLength;
    }

    public void setVideoLength(double videoLength) {
        this.videoLength = videoLength;
    }
}
