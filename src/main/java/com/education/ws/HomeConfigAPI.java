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
            @FormDataParam("file") InputStream fileInputStream,
            @FormDataParam("file") FormDataContentDisposition disposition) {
        String fileName = WSUtility.getUtf8Character(disposition.getFileName());
        logger.info("upload file " + fileName);
        homeConfigService.createImage(fileName, fileInputStream);
        return wsUtility.buildResponse();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getHomeImages() {
        List<HomeConfigResp> homeImages = homeConfigService.getHomeImages();
        return wsUtility.buildResponse(homeImages);
    }

    @DELETE
    @Path("{id}")
    public Response deleteHomeImage(@PathParam("id") int id){
        homeConfigService.deleteImage(id);
        return Response.ok().build();
    }
}
