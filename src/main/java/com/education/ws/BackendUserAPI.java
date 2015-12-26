package com.education.ws;

import com.education.formbean.BackendUserBean;
import com.education.service.BackendUserService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Created by yzzhao on 12/26/15.
 */
@Path("/backend_users")
public class BackendUserAPI {

    @Autowired
    private BackendUserService backendUserService;

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response createNewUser(@BeanParam BackendUserBean userBean) {
        backendUserService.createNewUser(userBean);
        return Response.ok().build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllUsers() {
        return Response.ok(backendUserService.getAllUsers()).build();
    }

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUser(@QueryParam("id") int id) {
        return Response.ok(backendUserService.queryUser(id)).build();
    }

    @DELETE
    @Path("{id}")
    public Response deleteUser(@PathParam("id") int id) {
        backendUserService.deleteUser(id);
        return Response.ok().build();
    }

    @POST
    @Path("/edit")
    public Response editUser(@BeanParam BackendUserBean userBean) {
        backendUserService.editUser(userBean);
        return Response.ok().build();
    }
}
