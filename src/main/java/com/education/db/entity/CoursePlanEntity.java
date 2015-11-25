package com.education.db.entity;

import com.education.formbean.CoursePlanBean;

import javax.persistence.*;

/**
 * Created by yzzhao on 11/25/15.
 */
@Table(name="course_plan")
@Entity
public class CoursePlanEntity {
    private int id;

    private String title;

    private String subTitle;

    private String content;

    private double price;

    public CoursePlanEntity() {
    }

    public CoursePlanEntity(CoursePlanBean bean){
        setTitle(bean.getTitle());
        setContent(bean.getContent());
        setSubTitle(bean.getSubTitle());
        setPrice(Double.parseDouble(bean.getPrice()));
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

    @Column(name = "title")
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Column(name = "sub_title")
    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    @Column(name = "content")
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Column(name = "price")
    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
