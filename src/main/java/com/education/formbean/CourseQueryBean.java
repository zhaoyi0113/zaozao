package com.education.formbean;

import com.education.db.entity.CommonStatus;
import com.education.db.entity.CourseEntity;
import com.education.db.entity.CourseTagEntity;
import com.education.ws.util.WSUtility;

import javax.ws.rs.FormParam;
import java.util.List;

/**
 * Created by yzzhao on 12/24/15.
 */
public class CourseQueryBean {

    private String id;

    private String name;

    private String content;

    private String date;

    private String titleImagePath;

    private String introduction;

    private String titleImageUrl;

    private String videoUrl;

    private List<CourseTagBean> tags;

    private CommonStatus status;

    private String publishDate;

    private double videoLength;

    private long pv;

    private boolean favorited;

    public CourseQueryBean(CourseEntity courseEntity, WSUtility wsUtility){
        this.id = String.valueOf(courseEntity.getId());
        this.name = courseEntity.getName();
        this.content = courseEntity.getContent();
        this.introduction = courseEntity.getIntroduction();
        this.videoUrl = wsUtility.getResourcePath(courseEntity.getId(), courseEntity.getVideoPath());
        if (courseEntity.getTitleImagePath() != null) {
            this.titleImageUrl = wsUtility.getResourcePath(courseEntity.getId(), courseEntity.getTitleImagePath());
        }
        status=courseEntity.getStatus();
        publishDate = wsUtility.dateToString(courseEntity.getPublishDate());
        videoLength = courseEntity.getVideoLength();
        pv  = courseEntity.getPv();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTitleImagePath() {
        return titleImagePath;
    }

    public void setTitleImagePath(String titleImagePath) {
        this.titleImagePath = titleImagePath;
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

    public List<CourseTagBean> getTags() {
        return tags;
    }

    public void setTags(List<CourseTagBean> tags) {
        this.tags = tags;
    }

    public CommonStatus getStatus() {
        return status;
    }

    public void setStatus(CommonStatus status) {
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

    public boolean isFavorited() {
        return favorited;
    }

    public void setFavorited(boolean favorited) {
        this.favorited = favorited;
    }
}
