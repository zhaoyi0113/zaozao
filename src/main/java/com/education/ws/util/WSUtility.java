package com.education.ws.util;

import org.glassfish.jersey.media.multipart.FormDataBodyPart;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by yzzhao on 11/5/15.
 */
@Component
public class WSUtility {

    private static final Logger logger = Logger.getLogger(WSUtility.class.getName());

    @Value("#{config['course_image_path']}")
    private String courseImagePath;

    @Value("#{config['course_image_url']}")
    private String courseImageUrl;

    public boolean whetherLogin(HttpServletRequest request) {
        HttpSession session = request.getSession();
        return session.getAttribute("user_name") != null;
    }

    public static String dateToString(Date date) {
        if (date == null) {
            return null;
        }
        try {
            SimpleDateFormat format = getDateFormat();
            return format.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Date stringToDate(String dateStr) {
        if (dateStr == null) {
            return null;
        }
        try {
            SimpleDateFormat format = getDateFormat();
            Date date = format.parse(dateStr);
            format.format(date);
            return date;
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage(),e);
        }
        return null;
    }

    public static SimpleDateFormat getDateFormat() {
        return new SimpleDateFormat(getDateFormatString());
    }

    public static String getDateFormatString() {
        return "yyyy/MM/dd";
    }

    public String getResourcePath(int courseId, String fileName) {
        return courseImageUrl + "/"+courseId+"/" + fileName;
    }

    public String getResourcePhysicalPath(String fileName) {
        return courseImagePath + "/" + fileName;
    }

    public boolean whetherVideo(String fileName) {
        if (fileName != null) {
            int index = fileName.lastIndexOf(".");
            if (index > 0) {
                String suffix = fileName.substring(index + 1);
                return suffix.equalsIgnoreCase("mp4");
            }
        }
        return false;
    }

    public void deleteCourseFile(String fileName) {
        String resourcePath = courseImagePath+"/"+fileName;
        File file = new File(resourcePath);
        logger.info("delete file " + file.getAbsolutePath()+", "+file.exists());
        boolean delete = file.delete();
        logger.info("delete file "+delete);
    }

    public void writeCourseFile(String fileName, InputStream inputStream, int courseId){

        writeFile(inputStream, courseImagePath+"/"+courseId, fileName);
    }

    public static String getFileNameFromMultipart(FormDataBodyPart multiPartFile) {
        return getUtf8Character(multiPartFile.getContentDisposition().getFileName());
    }

    public static String getUtf8Character(String str){
        String string = null;
        try {
            string = new String(str.getBytes("ISO-8859-1"),
                    "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return string;
    }

    public static void writeFile(InputStream input, String dir, String targetName) {
        try {
            if(input==null){
                return;
            }
            logger.info("write to file " + dir + "," + targetName);
            File dirFile = new File(dir);
            if (!dirFile.exists()) {
                dirFile.mkdirs();
            }
            FileOutputStream output = new FileOutputStream(dir + "/" + targetName);
            logger.info("save file in " + dir + "/" + targetName);
            byte buffer[] = new byte[512];
            int read = input.read(buffer);
            while (read > 0) {
                output.write(buffer, 0, read);
                read = input.read(buffer);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            logger.log(Level.SEVERE, e.getMessage(), e);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
