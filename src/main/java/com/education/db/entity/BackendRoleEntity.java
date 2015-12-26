package com.education.db.entity;

import javax.persistence.*;

/**
 * Created by yzzhao on 12/26/15.
 */
@Table(name = "backend_role")
@Entity
public class BackendRoleEntity {

    private int id;
    private String role;

    @Id
    @GeneratedValue
    @Column(name = "id")
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Column(name = "role")
    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
