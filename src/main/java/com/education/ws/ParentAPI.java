package com.education.ws;

import com.education.auth.Public;
import com.education.auth.TokenAccess;
import com.education.formbean.UserChildrenRegisterBean;
import com.education.service.ParentService;
import com.education.service.WeChatUserInfo;
import com.education.ws.util.ContextKeys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.ws.rs.*;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.logging.Logger;

/**
 * Created by yzzhao on 10/31/15.
 */
@Path("/userprofile")
@Service
public class ParentAPI {

    private static final Logger logger = Logger.getLogger(ParentAPI.class.getName());

    @Autowired
    private ParentService parentService;

//    @POST
//    @Path("/child")
//    @Public(requireWeChatCode = false, requireWeChatUser = true)
//    public Response updateUserProfile(@FormParam("user_name") String userName,
//                                      @BeanParam UserChildrenRegisterBean registerBean,
//                                      @Context ContainerRequestContext context) {
//        logger.info("register new child name " + registerBean.getChildName());
//        parentService.addUserChild(userName, registerBean, (WeChatUserInfo) context.getProperty(ContextKeys.WECHAT_USER));
//        return Response.ok().build();
//    }

    @POST
    @TokenAccess
    public Response updateUserProfile(@FormParam("user_name") String userName,
                              @BeanParam UserChildrenRegisterBean registerBean,
                              @Context ContainerRequestContext context) {
        logger.info("edit child name " + registerBean.getChildName());
        WeChatUserInfo user = (WeChatUserInfo) context.getProperty(ContextKeys.WECHAT_USER);
        logger.info("update user id "+user.getUserId());
        parentService.updateUserProfile(userName, registerBean, user);
        return Response.ok().build();
    }

    @GET
    @TokenAccess
    @Produces(MediaType.APPLICATION_JSON)
    public Response getChild(@Context ContainerRequestContext context) {
        WeChatUserInfo userInfo = (WeChatUserInfo) context.getProperty(ContextKeys.WECHAT_USER);
        UserChildrenRegisterBean userChild = parentService.getUserChild(userInfo.getUserId());
        return Response.ok(userChild).build();
    }

    @POST
    @Path("/headimage")
    @TokenAccess
    public Response updateUserProfile(){

        return Response.ok().build();
    }
}
