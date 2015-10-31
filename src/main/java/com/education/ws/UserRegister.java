package com.education.ws;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

/**
 * Created by yzzhao on 10/31/15.
 */
@Path("/register")
public class UserRegister {

    @POST
    @Path("/new")
    @Produces(MediaType.TEXT_PLAIN)
    public String registerNewUser(@BeanParam RegisterBean registerBean){

        return null;
    }
}
