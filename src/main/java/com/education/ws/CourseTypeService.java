package com.education.ws;

import com.education.db.DBConnection;
import com.education.db.entity.CourseTypeEntity;
import com.education.db.jpa.CourseTypeRepository;
import com.google.gson.Gson;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by yzzhao on 11/6/15.
 */
@Path("/coursetype")
public class CourseTypeService {

    private static final Logger logger = Logger.getLogger(CourseTypeService.class.getName());

    @Autowired
    private CourseTypeRepository courseTypeRepository;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllCaurseTypes(){
        try{
            Iterable<CourseTypeEntity> iterable = courseTypeRepository.findAll();
            List<String> list =new ArrayList<>();
            for(CourseTypeEntity entity : iterable){
                list.add(entity.getName());
                System.out.println("course type "+entity.getName());
            }
            return Response.ok(list).build();
        }catch(Exception e){
            e.printStackTrace();
            logger.log(Level.SEVERE, e.getMessage(), e);
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        finally {
            DBConnection.closeSession();
        }
    }

    private String getAllCourseTypesFromDB() {
        Session currentSession = DBConnection.getCurrentSession();
        Query query = currentSession.createQuery("from CourseTypeEntity");
        List<CourseTypeEntity> list = query.list();
        List<String> names = new ArrayList<>();
        for(Iterator<CourseTypeEntity> iterator = list.iterator(); iterator.hasNext();){
            names.add(iterator.next().getName());
        }
        Gson gson = new Gson();
        return gson.toJson(list);
    }
}
