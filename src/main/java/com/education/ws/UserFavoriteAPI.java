package com.education.ws;

import com.education.auth.TokenAccess;
import com.education.db.entity.UserEntity;
import com.education.service.LoginHistoryService;
import com.education.service.UserFavoriteService;
import com.education.ws.util.HeaderKeys;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import java.util.logging.Logger;

/**
 * Created by yzzhao on 1/24/16.
 */
@Path("/favorite")
public class UserFavoriteAPI {
    private static final Logger logger = Logger.getLogger(UserFavoriteAPI.class.getName());

    @Autowired
    private LoginHistoryService historyService;

    @Autowired
    private UserFavoriteService favoriteService;

    @POST
    @TokenAccess
    public Response addFavorite(@Context ContainerRequestContext context,
                                @FormParam("course_id") int courseId) {
        String token = (String) context.getProperty(HeaderKeys.ACCESS_TOKEN);
        UserEntity userInfo = null;
        if (token != null) {
            logger.info("login user " + token);
            userInfo = historyService.getUserByToken(token);
        }
        if(userInfo != null){
            favoriteService.addFavorite(userInfo.getUnionid(), courseId);
        }else{
            logger.severe("not login can't add favorite");
        }
        return Response.ok().build();
    }
}
