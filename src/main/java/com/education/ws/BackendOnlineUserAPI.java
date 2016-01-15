package com.education.ws;

import com.education.auth.Login;
import com.education.service.BackendOnlineUserService;
import com.education.service.WeChatUserInfo;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.logging.Logger;

/**
 * Created by yzzhao on 11/1/15.
 */
@Path("/online")
@Login
public class BackendOnlineUserAPI {

    private static final Logger logger = Logger.getLogger(BackendOnlineUserAPI.class.getName());

    @Autowired
    private BackendOnlineUserService userService;

    @Path("/allusers")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response queryAllUsers(@Context HttpServletRequest request, @QueryParam("page_index") int pageIdx, @QueryParam("number") int number) {
        List<WeChatUserInfo> userList = userService.getUserList(pageIdx, number);
        return Response.ok(userList).build();
    }

}
