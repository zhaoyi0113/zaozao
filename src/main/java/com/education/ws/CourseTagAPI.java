package com.education.ws;

import com.education.formbean.CourseTagBean;
import com.education.service.CourseTagService;
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
 * Created by yzzhao on 12/24/15.
 */
@Path("/course_tags")
public class CourseTagAPI {

    private static final Logger logger = Logger.getLogger(CourseTagAPI.class.getName());

    @Autowired
    private CourseTagService courseTagService;

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response createCourseTag(@FormDataParam("tag_name") String tagName,
                                    @FormDataParam("course_tag_id") int courseTagId,
                                    @FormDataParam("file") InputStream fileInputStream,
                                    @FormDataParam("file") FormDataContentDisposition disposition) {
        logger.info("add course tag " + tagName);
        String fileName = WSUtility.getUtf8Character(disposition.getFileName());
        if(courseTagId == 0) {
            courseTagService.addNewCourseTag(tagName, fileName, fileInputStream);
        }else{
            courseTagService.editCourseTag(courseTagId, tagName, disposition.getFileName(), fileInputStream);
         }
        return WSUtility.buildResponse();
    }

    @POST
    @Path("/edit")
    public Response editCourseTag(@FormParam("tag_name") String tagName,
                                  @FormParam("course_tag_id") int courseTagId) {
        logger.info("edit course tag " + tagName);
        courseTagService.editCourseTag(courseTagId, tagName, null, null);
        return WSUtility.buildResponse();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response getCourseTags() {
        List<CourseTagBean> courseTags = courseTagService.getCourseTags();
        return WSUtility.buildResponse(courseTags);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/{courseId}")
    public Response getCourseTag(@PathParam("courseId") int courseTagId) {
        return WSUtility.buildResponse(courseTagService.getCourseTag(courseTagId));
    }

    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{id}")
    public Response deleteCourseTag(@PathParam("id") int courseTagId) {
        courseTagService.deleteCourseTag(courseTagId);
        return WSUtility.buildResponse();
    }
}
