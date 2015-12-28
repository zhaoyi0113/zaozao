package com.education.ws;

import com.education.auth.Login;
import com.education.formbean.BackendUserBean;
import com.education.service.BackendRoleService;
import com.education.service.BackendUserService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Created by yzzhao on 12/26/15.
 */
@Path("/backend_users")
@Login
public class BackendUserAPI {

    @Autowired
    private BackendUserService userService;

    @Autowired
    private BackendRoleService roleService;

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response createNewUser(@BeanParam BackendUserBean userBean) {
        userService.createNewUser(userBean);
        return Response.ok().build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllUsers() {
        return Response.ok(userService.getAllUsers()).build();
    }

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUser(@QueryParam("id") int id) {
        return Response.ok(userService.queryUser(id)).build();
    }

    @DELETE
    @Path("{id}")
    public Response deleteUser(@PathParam("id") int id) {
        userService.deleteUser(id);
        return Response.ok().build();
    }

    @POST
    @Path("/edit")
    public Response editUser(@BeanParam BackendUserBean userBean) {
        userService.editUser(userBean);
        return Response.ok().build();
    }

    @GET
    @Path("/get_role_names")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllRoleNames(){
        return Response.ok(roleService.getRoleNames()).build();
    }

    @GET
    @Path("/get_roles")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllRoles(){
        return Response.ok(roleService.getRoles()).build();
    }
}
