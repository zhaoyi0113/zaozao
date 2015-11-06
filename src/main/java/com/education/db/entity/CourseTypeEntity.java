package com.education.db.entity;

import javax.persistence.*;

/**
 * Created by yzzhao on 11/6/15.
 */
@Entity
@Table(name="course_type")
public class CourseTypeEntity {
    private int id;
    private String name;

    @Id
    @GeneratedValue
    @Column(name = "id")
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Column(name="name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
