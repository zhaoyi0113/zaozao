package com.education.ws;

import org.hibernate.SessionFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.Date;

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

    public static String dateToString(Date date){
        try{
            SimpleDateFormat format = WSUtility.getDateFormat();
            return format.format(date);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    private static Date stringToDate(String dateStr){
        try{
            SimpleDateFormat format = WSUtility.getDateFormat();
            Date date = format.parse(dateStr);
            format.format(date);
            return date;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }


    public static SimpleDateFormat getDateFormat(){
        return new SimpleDateFormat(getDateFormatString());
    }

    public static String getDateFormatString() {
        return "yyyy-MM-dd HH:mm:ss";
    }
}
