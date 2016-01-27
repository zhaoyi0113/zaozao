package com.education.ws;

import com.education.formbean.UserProfilePrivilegeBean;
import com.education.service.UserProfilePrivilegeService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Created by yzzhao on 1/27/16.
 */
@Path("/user_profile")
public class UserProfilePrivilegeAPI {

    @Autowired
    private UserProfilePrivilegeService service;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUserProfilePrivilege() {
        return Response.ok(service.getUserProfilePrivilege()).build();
    }

    @POST
    public Response editUserProfilePrivilege(@BeanParam UserProfilePrivilegeBean bean) {
        service.editUserProfilePrivilege(bean);
        return Response.ok().build();
    }
}
