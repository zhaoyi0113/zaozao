package com.education.ws;

import javax.ws.rs.FormParam;

/**
 * Created by yzzhao on 10/31/15.
 */
public class RegisterBean {
    @FormParam("userName")
    private String userName;

    @FormParam("password")
    private String password;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        StringBuffer buffer =new StringBuffer();
        buffer.append("userName=").append(userName).append("\nPassword=").append(password);
        return buffer.toString();
    }

}
