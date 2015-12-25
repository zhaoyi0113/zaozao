package com.education.db.entity;

import javax.persistence.*;

/**
 * Created by yzzhao on 12/24/15.
 */
@Entity
@Table(name = "course_tag")
public class CourseTagEntity {

    private int id;
    private String name;
    private String image;

    @Column(name = "id")
    @Id
    @GeneratedValue
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

    @Column(name = "image")
    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
