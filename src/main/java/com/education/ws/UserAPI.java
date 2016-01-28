package com.education.ws;

import com.education.auth.TokenAccess;
import com.education.formbean.UserChildrenRegisterBean;
import com.education.service.ParentService;
import com.education.service.UserProfilePrivilegeService;
import com.education.service.UserService;
import com.education.service.WeChatUserInfo;
import com.education.ws.util.ContextKeys;
import com.education.ws.util.HeaderKeys;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.logging.Logger;

/**
 * Created by yzzhao on 1/24/16.
 */
@Path("/user")
public class UserAPI {

    private static final Logger logger = Logger.getLogger(UserAPI.class.getName());
    @Autowired
    private UserService userService;

    @Autowired
    private ParentService parentService;

    @Autowired
    private UserProfilePrivilegeService privilegeService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @TokenAccess(requireAccessToken = true)
    public Response getUserInfo(@Context ContainerRequestContext context) {
        WeChatUserInfo userInfo  = (WeChatUserInfo) context.getProperty(ContextKeys.WECHAT_USER);
        UserChildrenRegisterBean child = parentService.getUserChild(userInfo.getUserId());
        userInfo.setChild(child);
        userInfo.setPrivilege(privilegeService.getUserProfilePrivilege());
        return Response.ok(userInfo).build();
    }

}
