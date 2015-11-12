package com.education.db.entity;
import com.education.ws.ReserveCourseBean;

import javax.persistence.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by yzzhao on 11/12/15.
 */
@Table(name = "reservation_course")
@Entity
public    class ReserveCourseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "mobile")
    private String mobile;

    @Column(name = "name")
    private String name;

    @Column(name = "date")
    private Date date;

    @Column(name="time_created")
    private Date timeCreated;

    @Column(name = "children_birthedate")
    private Date childBirthdate;

    public ReserveCourseEntity(){

    }

    public ReserveCourseEntity(ReserveCourseBean bean){
        setName(bean.getName());
        setMobile(bean.getMobile());
        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        try {
            setDate(format.parse(bean.getDate()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        try {
            setChildBirthdate(format.parse(bean.getChildBirthdate()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Date getTimeCreated() {
        return timeCreated;
    }

    public void setTimeCreated(Date timeCreated) {
        this.timeCreated = timeCreated;
    }

    public Date getChildBirthdate() {
        return childBirthdate;
    }

    public void setChildBirthdate(Date childBirthdate) {
        this.childBirthdate = childBirthdate;
    }
}
