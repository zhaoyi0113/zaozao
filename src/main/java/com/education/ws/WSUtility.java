package com.education.ws;

import org.hibernate.SessionFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;

/**
 * Created by yzzhao on 11/5/15.
 */
public final class WSUtility {
    private WSUtility(){
        SessionFactory s;
    }

    public static boolean whetherLogin(HttpServletRequest request){
        HttpSession session = request.getSession();
        return session.getAttribute("user_name") != null;
    }

    public static SimpleDateFormat getDateFormat(){
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    }
}
