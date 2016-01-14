package com.education.ws;

import com.education.auth.TokenAccess;
import com.education.db.entity.UserEntity;
import com.education.service.CourseInteractiveService;
import com.education.service.LoginHistoryService;
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
 * Created by yzzhao on 1/13/16.
 */
@Path("/course/interactive")
public class CourseInteractiveAPI {

    private static final Logger logger = Logger.getLogger(CourseInteractiveAPI.class.getName());

    @Autowired
    private LoginHistoryService historyService;

    @Autowired
    private CourseInteractiveService interactiveService;

    @POST
    @TokenAccess
    public Response activeCourse(@Context ContainerRequestContext context,
                                 @FormParam("course_id") int courseId, @FormParam("flag") String flag) {
        String token = (String) context.getProperty(HeaderKeys.ACCESS_TOKEN);
        UserEntity userInfo = null;
        if (token != null) {
            logger.info("login user " + token);
            userInfo = historyService.getUserByToken(token);
        } else {
            logger.info("anonymous query ");
        }
        interactiveService.activeCourse(userInfo, courseId, flag);
        return Response.ok().build();
    }
}
