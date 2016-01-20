package com.education.db.entity;

import javax.persistence.*;

/**
 * Created by yzzhao on 1/20/16.
 */
@Entity
@Table(name = "home_course")
public class HomeCourseEntity {

    private int id;
    private int courseId;
    private int orderIndex;

    @Id
    @GeneratedValue
    @Column(name = "id")
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Column(name = "course_id")
    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    @Column(name = "order_index")
    public int getOrderIndex() {
        return orderIndex;
    }

    public void setOrderIndex(int orderIndex) {
        this.orderIndex = orderIndex;
    }
}
