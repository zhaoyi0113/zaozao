package com.education.ws;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * Created by yzzhao on 11/5/15.
 */
public final class WSUtility {
    private WSUtility(){

    }

    public static boolean whetherLogin(HttpServletRequest request){
        HttpSession session = request.getSession();
        return session.getAttribute("user_name") != null;
    }
}
