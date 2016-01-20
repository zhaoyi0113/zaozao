package com.education.ws;

import com.education.formbean.HomeConfigResp;
import com.education.formbean.HomeCourseBean;
import com.education.service.HomeCourseService;
import com.education.ws.util.WSUtility;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.InputStream;
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

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getHomeImages() {
        List<HomeCourseBean> homeImages = homeCourseService.getHomeCourses();
        return Response.ok(homeImages).build();
    }

    @DELETE
    @Path("{id}")
    public Response deleteHomeImage(@PathParam("id") int id) {
        homeCourseService.deleteCourse(id);
        return Response.ok().build();
    }

    @POST
    @Path("/move")
    public Response moveItem(@FormParam("id") int id, @FormParam("action") String action) {
        homeCourseService.moveAction(id, action);
        return Response.ok().build();
    }
}
