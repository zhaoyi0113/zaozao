package com.education.ws;

import com.education.auth.Login;
import com.education.auth.Public;
import com.education.service.CourseVideoService;
import com.education.ws.util.WSUtility;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.InputStream;

/**
 * Created by yzzhao on 1/5/16.
 */
@Path("/course/video")
public class CourseVideoAPI {

    @Autowired
    private CourseVideoService videoService;

    @Path("/{id}")
    @GET
    public Response getCourseVideo(@PathParam("id") int id) {
        String videoUrl = videoService.getVideoUrl(id);
        return Response.ok(videoUrl).build();
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response pushCourseVideo(@FormDataParam("course_id") int courseId,
                                    @FormDataParam("file") InputStream fileInputStream,
                                    @FormDataParam("file") FormDataContentDisposition disposition) {
        String fileName = WSUtility.getUtf8Character(disposition.getFileName());
        videoService.setVideo(courseId, fileName, fileInputStream);
        return Response.ok().build();
    }


}
