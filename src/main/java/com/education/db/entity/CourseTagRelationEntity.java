package com.education.db.entity;

import javax.persistence.*;

/**
 * Created by yzzhao on 12/23/15.
 */

@Entity
@Table(name = "course_tag_rel")
public class CourseTagRelationEntity {
    private int id;
    private int courseId;
    private int courseTagId;

    @Column(name="course_id")
    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    @Column(name="course_tag_id")
    public int getCourseTagId() {
        return courseTagId;
    }

    public void setCourseTagId(int courseTagId) {
        this.courseTagId = courseTagId;
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
}
