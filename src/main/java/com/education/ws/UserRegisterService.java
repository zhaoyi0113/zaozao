package com.education.ws;

import com.education.db.DBConnection;
import com.education.db.entity.UserEntity;
import org.hibernate.Session;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.logging.Logger;

/**
 * Created by yzzhao on 10/31/15.
 */
@Path("/register")
public class UserRegisterService {

    private static final Logger logger = Logger.getLogger(UserRegisterService.class.getName());

    @POST
    @Path("/new")
    @Produces(MediaType.TEXT_PLAIN)
    /**
     * http://localhost:8080/education/zaozao/register/new
     */
    public Response registerNewUser(@BeanParam UserRegisterBean registerBean) {
        System.out.println("register new user " + registerBean);
        logger.info("register new user " + registerBean);
        try {
            ResponseStatus status = registerUser(registerBean);
            if(status == ResponseStatus.SUCCESS){
                return Response.ok().build();
            } else if(status == ResponseStatus.USER_EXISTED){
                return Response.status(Response.Status.BAD_REQUEST).entity("User already existed.").build();
            }
        } finally {
            DBConnection.closeSession();
        }
        return Response.status(Response.Status.BAD_REQUEST).build();
    }

    public ResponseStatus registerUser(UserRegisterBean registerBean) {
        if(isExistedUser(registerBean.getUserName())){
            return ResponseStatus.USER_EXISTED;
        }
        Session session = null;
        session = DBConnection.getCurrentSession();
        session.beginTransaction();
        UserEntity userEntity = new UserEntity(registerBean);
        session.save(userEntity);
        session.getTransaction().commit();
        return ResponseStatus.SUCCESS;
    }

    private boolean isExistedUser(String userName){
        UserRegisterBean user = UserQuery.getUserByName(userName);
        return user!=null;
    }

    public enum ResponseStatus{
        USER_EXISTED(1), SUCCESS(0);

        ResponseStatus(int i) {

        }
    }
}
