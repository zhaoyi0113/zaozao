package com.education;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * Created by yzzhao on 10/31/15.
 */
@Path("/register")
public class UserRegister {

    @POST
    @Path("/new")
    @Produces(MediaType.TEXT_PLAIN)
    public String registerNewUser(){

        return null;
    }
}
