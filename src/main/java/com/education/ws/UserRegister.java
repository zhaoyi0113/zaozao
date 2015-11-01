package com.education.ws;

import com.education.db.DBConnection;
import org.hibernate.Session;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.logging.Logger;

/**
 * Created by yzzhao on 10/31/15.
 */
@Path("/register")
public class UserRegister {

    private static final Logger logger = Logger.getLogger(UserRegister.class.getName());

    @POST
    @Path("/new")
    @Produces(MediaType.TEXT_PLAIN)
    public String registerNewUser(@BeanParam RegisterBean registerBean){
        System.out.println("register new user "+registerBean);
        logger.info("register new user "+registerBean);
        Session session =  DBConnection.getCurrentSession();
        
        return "";
    }
}
