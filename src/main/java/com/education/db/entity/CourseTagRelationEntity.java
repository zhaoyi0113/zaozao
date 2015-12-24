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
    private int courseTypeId;

    @Column(name="course_id")
    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    @Column(name="course_type_id")
    public int getCourseTypeId() {
        return courseTypeId;
    }

    public void setCourseTypeId(int courseTypeId) {
        this.courseTypeId = courseTypeId;
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
