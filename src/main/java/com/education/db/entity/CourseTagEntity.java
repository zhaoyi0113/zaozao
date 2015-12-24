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
}
