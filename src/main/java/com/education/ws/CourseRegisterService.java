package com.education.ws;

import com.education.db.DBConnection;
import com.education.db.entity.CourseEntity;
import com.google.gson.Gson;
import org.glassfish.jersey.media.multipart.FormDataBodyPart;
import org.glassfish.jersey.media.multipart.FormDataMultiPart;
import org.hibernate.Query;
import org.hibernate.Session;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by yzzhao on 11/3/15.
 */
@Path("course")
public class CourseRegisterService {

    private static final Logger logger = Logger.getLogger(CourseRegisterService.class.getName());

    @Path("new")
    @POST
    public Response createNewCourse(@BeanParam CourseRegisterBean bean){
        System.out.println("create new course "+bean);
        logger.info("create new course "+bean);
        try{
            createCourse(bean);
        }catch(Exception e){
            logger.log(Level.SEVERE, e.getMessage(), e);
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }finally{
            DBConnection.closeSession();
        }
        return Response.ok().build();
    }

    @Path("uploadfile")
    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces("multipart/mixed")
    public Response uploadFile(FormDataMultiPart multiPart){
        CourseRegisterBean bean = new CourseRegisterBean();
        bean.setName(multiPart.getField("name").getValue());
        bean.setCategory(multiPart.getField("category").getValue());
        bean.setDate(multiPart.getField("date").getValue());
        bean.setContent(multiPart.getField("content").getValue());
        int id = createNewCourseWithBean(bean);
        InputStream file = multiPart.getField("file").getValueAs(InputStream.class);
        String imageDir = System.getProperty("COURSE_IMAGE_DIR");
        if(imageDir == null){
            imageDir = "target/courses/";
        }
        imageDir += "/"+id;
        writeFile(file, imageDir, bean.getName());
        return Response.ok().build();
    }

    @Path("queryall")
    @GET
    public Response getAllCourses(){
        try{
            List<CourseEntity> allCourse = getAllCourse();
            Gson gson = new Gson();
            String json = gson.toJson(allCourse);
            return Response.ok().entity(json).build();
        }catch(Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        } finally{
            DBConnection.closeSession();
        }
    }

    @Path("query/allnames")
    @GET
    public Response getAllCourseNames(){
        try{
            List<CourseEntity> allCourse = getAllCourse();
            List<String> names = new ArrayList<>();
            for(Iterator<CourseEntity> iterator = allCourse.iterator(); iterator.hasNext();){
                CourseEntity next = iterator.next();
                names.add(next.getName());
            }
            Gson gson = new Gson();
            String json = gson.toJson(names);
            return Response.ok().entity(json).build();
        }catch(Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        } finally{
            DBConnection.closeSession();
        }
    }

    private static int createCourse(CourseRegisterBean bean){
        Session currentSession = DBConnection.getCurrentSession();
        CourseEntity entity = new CourseEntity(bean);
        currentSession.beginTransaction();
        int save = (int)currentSession.save(entity);
        currentSession.getTransaction().commit();
        return save;
    }

    private static List<CourseEntity> getAllCourse(){
        Session currentSession = DBConnection.getCurrentSession();
        Query query = currentSession.createQuery("from CourseEntity");
        List<CourseEntity> courses = query.list();
        return courses;
    }

    private int createNewCourseWithBean(CourseRegisterBean bean){
        System.out.println("create new course "+bean);
        logger.info("create new course "+bean);
        try{
            int id = createCourse(bean);
            return id;
        }catch(Exception e){
            logger.log(Level.SEVERE, e.getMessage(), e);
        }finally{
            DBConnection.closeSession();
        }
        return -1;
    }

    private static void writeFile(InputStream input, String dir,String targetName){
        try {
            File dirFile = new File(dir);
            if(!dirFile.exists()){
                dirFile.mkdirs();
            }
            FileOutputStream output =new FileOutputStream(dir+"/"+targetName);
            byte buffer[] = new byte[512];
            int read = input.read(buffer);
            while(read > 0){
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
