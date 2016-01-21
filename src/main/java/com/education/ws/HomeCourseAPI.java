package com.education.ws;

import com.education.formbean.HomeCourseBean;
import com.education.service.HomeCourseService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.logging.Logger;

/**
 * Created by yzzhao on 1/20/16.
 */
@Path("/homecourse")
public class HomeCourseAPI {

    private static final Logger logger = Logger.getLogger(HomeCourseAPI.class.getName());

    @Autowired
    private HomeCourseService homeCourseService;

    @POST
    public Response addNewHomeCourse(@FormParam("course_id") int courseId) {
        logger.info("add new home course " + courseId);
        homeCourseService.addCourseOnHomePage(courseId);
        return Response.ok().build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getHomeCourses() {
        List<HomeCourseBean> homeImages = homeCourseService.getHomeCourses();
        return Response.ok(homeImages).build();
    }

    @DELETE
    @Path("{id}")
    public Response deleteHomeCourse(@PathParam("id") int id) {
        homeCourseService.deleteCourse(id);
        return Response.ok().build();
    }

    @POST
    @Path("/move")
    public Response moveCourse(@FormParam("id") int id, @FormParam("action") String action) {
        homeCourseService.moveAction(id, action);
        return Response.ok().build();
    }
}
