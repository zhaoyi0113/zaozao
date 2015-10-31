package com.education;

import org.glassfish.jersey.server.ResourceConfig;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

/**
 * Created by yzzhao on 10/31/15.
 */
@ApplicationPath("/")
@Path("/hello")
public class MyApplication extends ResourceConfig {

    public MyApplication(){
        packages("com.education");
    }

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String sayHello() {
        return "Hello World!" ;
    }

    @GET
    @Path("/{param}")
    @Produces(MediaType.TEXT_PLAIN)
    public String sayHelloTo(@PathParam("param") String name){
        return "Hello "+name;
    }
}
