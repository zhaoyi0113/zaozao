package com.education.ws;

import com.education.auth.TokenAccess;
import com.education.service.UserFavoriteService;
import com.education.service.WeChatUserInfo;
import com.education.ws.util.ContextKeys;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.*;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.logging.Logger;

/**
 * Created by yzzhao on 1/24/16.
 */
@Path("/favorite")
public class UserFavoriteAPI {
    private static final Logger logger = Logger.getLogger(UserFavoriteAPI.class.getName());

    @Autowired
    private UserFavoriteService favoriteService;

    @POST
    @TokenAccess
    public Response addFavorite(@Context ContainerRequestContext context,
                                @FormParam("course_id") int courseId) {
        WeChatUserInfo userInfo = (WeChatUserInfo) context.getProperty(ContextKeys.WECHAT_USER);
        if (userInfo != null) {
            favoriteService.addFavorite(userInfo.getUserId(), courseId);
        } else {
            logger.severe("not login can't add favorite");
        }
        return Response.ok().build();
    }

    @GET
    @TokenAccess
    @Produces(MediaType.APPLICATION_JSON)
    public Response getFavorite(@Context ContainerRequestContext context,
                                @DefaultValue("5") @QueryParam("number") int number,
                                @DefaultValue("0") @QueryParam("page_index") int pageIndex) {
        WeChatUserInfo userInfo = (WeChatUserInfo) context.getProperty(ContextKeys.WECHAT_USER);
        return Response.ok(favoriteService.getUserFavoriteCourses(userInfo.getUserId(), pageIndex, number)).build();
    }

}
