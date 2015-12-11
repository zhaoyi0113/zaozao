package com.education.ws.util;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by yzzhao on 11/5/15.
 */
@Component
public class WSUtility {

    @Value("#{config['course_image_path']}")
    private String courseImagePath;

    @Value("#{config['course_image_url']}")
    private String courseImageUrl;

    public boolean whetherLogin(HttpServletRequest request){
        HttpSession session = request.getSession();
        return session.getAttribute("user_name") != null;
    }

    public String dateToString(Date date){
        try{
            SimpleDateFormat format = getDateFormat();
            return format.format(date);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public static Date stringToDate(String dateStr){
        try{
            SimpleDateFormat format = getDateFormat();
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

    public String getResourcePath(String fileName){
        return courseImageUrl+"/"+fileName;
    }

    public String getResourcePhysicalPath(String fileName){
        return courseImagePath + "/" +fileName;
    }

    public boolean whetherVideo(String fileName){
        if(fileName != null){
            int index = fileName.lastIndexOf(".");
            if(index >0) {
                String suffix = fileName.substring(index+1);
                return suffix.equalsIgnoreCase("mp4");
            }
        }
        return false;
    }

    public void deleteFile(String fileName){
        String resourcePath = getResourcePath(fileName);
        File file = new File(resourcePath);
        System.out.println("delete file "+file.getAbsolutePath());
        file.delete();
    }
}
