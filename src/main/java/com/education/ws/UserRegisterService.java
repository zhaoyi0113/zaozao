package com.education.ws;

import com.education.db.DBConnection;
import com.education.db.entity.UserEntity;
import com.education.db.jpa.UserRepository;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.ws.rs.BeanParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.logging.Logger;

/**
 * Created by yzzhao on 10/31/15.
 */
@Path("/register")
@Service
public class UserRegisterService {

    private static final Logger logger = Logger.getLogger(UserRegisterService.class.getName());

    @Autowired
    private UserRepository userRepository;

    @POST
    @Path("/newuser")
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
//        if(isExistedUser(registerBean.getUserName())){
//            return ResponseStatus.USER_EXISTED;
//        }
//        Session session = null;
//        session = DBConnection.getCurrentSession();
//        session.beginTransaction();
        UserEntity userEntity = new UserEntity(registerBean);
        userRepository.save(userEntity);
//        session.save(userEntity);
//        session.getTransaction().commit();
        return ResponseStatus.SUCCESS;
    }

    private boolean isExistedUser(String userName){
        UserRegisterBean user = UserQuery.getUserByName(userName);
        return user!=null;
    }

}
