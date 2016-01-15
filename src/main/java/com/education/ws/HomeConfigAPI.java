package com.education.ws;

import com.education.formbean.HomeConfigResp;
import com.education.service.HomeConfigService;
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
 * Created by yzzhao on 12/25/15.
 */
@Path("/homeconfig")
public class HomeConfigAPI {

    private static final Logger logger = Logger.getLogger(HomeConfigAPI.class.getName());

    @Autowired
    private HomeConfigService homeConfigService;

    @Autowired
    private WSUtility wsUtility;

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response addHomeImage(
            @FormDataParam("course_id") int courseId,
            @FormDataParam("file") InputStream fileInputStream,
            @FormDataParam("file") FormDataContentDisposition disposition) {
        String fileName = WSUtility.getUtf8Character(disposition.getFileName());
        logger.info("upload file " + fileName);
        homeConfigService.createImage(fileName, courseId, fileInputStream);
        return Response.ok().build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getHomeImages() {
        List<HomeConfigResp> homeImages = homeConfigService.getHomeImages();
        return Response.ok(homeImages).build();
    }

    @DELETE
    @Path("{id}")
    public Response deleteHomeImage(@PathParam("id") int id) {
        homeConfigService.deleteImage(id);
        return Response.ok().build();
    }

    @POST
    @Path("/move")
    public Response moveItem(@FormParam("id") int id, @FormParam("action") String action) {
        homeConfigService.moveAction(id, action);
        return Response.ok().build();
    }
}
