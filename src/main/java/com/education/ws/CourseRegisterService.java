package com.education.ws;

import com.education.db.DBConnection;
import com.education.db.entity.CourseEntity;
import com.google.gson.Gson;
import org.hibernate.Query;
import org.hibernate.Session;

import javax.ws.rs.BeanParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
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
            Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }finally{
            DBConnection.closeSession();
        }
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

    private static void createCourse(CourseRegisterBean bean){
        Session currentSession = DBConnection.getCurrentSession();
        CourseEntity entity = new CourseEntity(bean);
        currentSession.beginTransaction();
        currentSession.save(entity);
        currentSession.getTransaction().commit();
    }

    private static List<CourseEntity> getAllCourse(){
        Session currentSession = DBConnection.getCurrentSession();
        Query query = currentSession.createQuery("from CourseEntity");
        List<CourseEntity> courses = query.list();
        return courses;
    }

}
