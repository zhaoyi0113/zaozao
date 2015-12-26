package com.education.db.entity;

import javax.persistence.*;

/**
 * Created by yzzhao on 12/25/15.
 */
@Entity
@Table(name = "homeconfig")
public class HomeConfigEntity {

    private int id;
    private String image;

    @Id
    @GeneratedValue
    @Column(name = "id")
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Column(name = "image")
    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
