package com.education.formbean;

import javax.ws.rs.FormParam;

/**
 * Created by yzzhao on 11/25/15.
 */
public class CoursePlanBean {

    @FormParam("id")
    private int id;

    @FormParam("title")
    private String title;

    @FormParam("sub_title")
    private String subTitle;

    @FormParam("content")
    private String content;

    @FormParam("price")
    private String price;

    public String getTitle() {
        return title;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
