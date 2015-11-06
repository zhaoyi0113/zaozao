package com.education.ws;

import com.education.db.DBConnection;
import com.education.db.entity.CourseTypeEntity;
import com.google.gson.Gson;
import org.hibernate.Query;
import org.hibernate.Session;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
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

    @GET
    public String getAllCaurseTypes(){
        try{
            String ret = getAllCourseTypesFromDB();
            return ret;
        }catch(Exception e){
            e.printStackTrace();
            logger.log(Level.SEVERE, e.getMessage(), e);
            return e.getMessage();
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
