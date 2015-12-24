package com.education.ws;

import com.education.db.entity.CourseTagEntity;
import com.education.service.CourseTagService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Created by yzzhao on 12/24/15.
 */
@Path("/course_tags")
public class CourseTagAPI {

    @Autowired
    private CourseTagService courseTagService;

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createCourseTag(@FormParam("tag_name") String tagName) {
        courseTagService.addNewCourseTag(tagName);
        return Response.ok().build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response getCourseTags() {
        Iterable<CourseTagEntity> courseTags = courseTagService.getCourseTags();
        return Response.ok(courseTags).build();
    }
}
