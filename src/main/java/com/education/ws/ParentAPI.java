package com.education.ws;

import com.education.auth.Public;
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
@Path("/register")
@Service
public class ParentAPI {

    private static final Logger logger = Logger.getLogger(ParentAPI.class.getName());

    @Autowired
    private ParentService parentService;

    @POST
    @Path("/child")
    @Public(requireWeChatCode = false, requireWeChatUser = true)
    public Response registerChild(@BeanParam UserChildrenRegisterBean registerBean, @Context ContainerRequestContext context) {
        logger.info("register new child name " + registerBean.getChildName());
        parentService.registerUserChild(registerBean, (WeChatUserInfo) context.getProperty(ContextKeys.WECHAT_USER));
        return Response.ok().build();
    }

    @POST
    @Public(requireWeChatCode = false, requireWeChatUser = true)
    public Response editChild(@BeanParam UserChildrenRegisterBean registerBean, @Context ContainerRequestContext context) {
        logger.info("edit child name " + registerBean.getChildName());
        parentService.editUserChild(registerBean);
        return Response.ok().build();
    }

    @GET
    @Public(requireWeChatCode = false, requireWeChatUser = true)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getChild(@Context ContainerRequestContext context){
        UserChildrenRegisterBean userChild = parentService.getUserChild((WeChatUserInfo) context.getProperty(ContextKeys.WECHAT_USER));
        return Response.ok(userChild).build();
    }

}
