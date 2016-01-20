package com.education.ws;

import com.education.auth.Public;
import com.education.formbean.UserChildrenRegisterBean;
import com.education.service.UserService;
import com.education.service.WeChatUserInfo;
import com.education.ws.util.ContextKeys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.ws.rs.BeanParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
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
public class UserRegisterAPI {

    private static final Logger logger = Logger.getLogger(UserRegisterAPI.class.getName());

    @Autowired
    private UserService userService;

    @POST
    @Path("/child")
    @Public(requireWeChatCode = true, requireWeChatUser = false)
    @Produces(MediaType.TEXT_PLAIN)
    /**
     * http://localhost:8080/education/zaozao/register/child
     */
    public Response registerNewUser(@BeanParam UserChildrenRegisterBean registerBean, @Context ContainerRequestContext context) {
        logger.info("register new user " + registerBean.getOpenid());
        userService.registerUserChild(registerBean, (WeChatUserInfo) context.getProperty(ContextKeys.WECHAT_USER));
        return Response.ok().build();
    }


}
