package com.education.ws;

import com.education.auth.Login;
import com.education.service.BackendOnlineUserService;
import com.education.service.WeChatUserInfo;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.*;
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
    public Response queryAllUsers(@QueryParam("page_index") int pageIdx, @QueryParam("number") int number) {
        List<WeChatUserInfo> userList = userService.getUserList(pageIdx, number);
        return Response.ok(userList).build();
    }

    @Path("/user_count")
    @GET
    public Response queryAllUsersCount() {
        return Response.ok(userService.getUserCount()).build();
    }

    @Path("/user_access_history")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUserAccessCourseHistory(@QueryParam("user_id") int userId,
                                               @QueryParam("page_index") int pageIdx,
                                               @QueryParam("number") int number,
                                               @QueryParam("flag") String flag) {
        return Response.ok(userService.getUserAccessCourseHistory(userId, pageIdx, number, flag)).build();
    }

    @Path("/user_access_count")
    @GET
    public Response getUserAccessCount(@QueryParam("user_id") int userId){
        return Response.ok(userService.getUserAccessCount(userId)).build();
    }
}
