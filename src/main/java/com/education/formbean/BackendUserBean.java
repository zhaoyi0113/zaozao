package com.education.formbean;

import javax.ws.rs.FormParam;

/**
 * Created by yzzhao on 12/26/15.
 */
public class BackendUserBean {
    @FormParam("id")
    private int id;
    @FormParam("name")
    private String name;
    @FormParam("password")
    private String password;
    @FormParam("roleId")
    private int roleId;
    @FormParam("email")
    private String email;

    public BackendUserBean(String name, String password, int roleId) {
        this.name = name;
        this.password = password;
        this.roleId = roleId;
    }

    public BackendUserBean() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getRoleId() {
        return roleId;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
