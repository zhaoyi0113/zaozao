package com.education.ws;

import com.education.db.DBConnection;
import com.education.db.entity.UserEntity;
import com.education.db.jpa.UserRepository;
import com.education.formbean.UserChildrenRegisterBean;
import com.education.service.UserService;
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
public class UserRegisterAPI {

    private static final Logger logger = Logger.getLogger(UserRegisterAPI.class.getName());

    @Autowired
    private UserService userService;

    @POST
    @Path("/newuser")
    @Produces(MediaType.TEXT_PLAIN)
    /**
     * http://localhost:8080/education/zaozao/register/new
     */
    public Response registerNewUser(@BeanParam UserChildrenRegisterBean registerBean) {
        logger.info("register new user " + registerBean.getOpenid());
        userService.registerUserChild(registerBean);
        return Response.ok().build();
    }


}
