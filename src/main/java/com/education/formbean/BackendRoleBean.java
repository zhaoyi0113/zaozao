package com.education.formbean;

/**
 * Created by yzzhao on 12/28/15.
 */
public class BackendRoleBean {
    private int id;
    private String roleName;

    public BackendRoleBean(int id, String roleName) {
        this.id = id;
        this.roleName = roleName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }
}
