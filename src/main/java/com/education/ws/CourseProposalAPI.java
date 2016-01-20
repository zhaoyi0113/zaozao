package com.education.ws;


import com.education.auth.TokenAccess;
import com.education.db.entity.UserEntity;
import com.education.formbean.CourseQueryBean;
import com.education.service.CourseProposalService;
import com.education.service.CourseService;
import com.education.service.LoginHistoryService;
import com.education.service.WeChatUserInfo;
import com.education.ws.util.ContextKeys;
import com.education.ws.util.HeaderKeys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

@Path("course/proposal")
@EnableTransactionManagement
@Transactional
@Service
public class CourseProposalAPI {

    private static final Logger logger = Logger.getLogger(CourseProposalAPI.class.getName());

    @Autowired
    private CourseProposalService courseProposalService;

    @Autowired
    private CourseService courseService;

    @Autowired
    private LoginHistoryService historyService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/query")
    public Response getCourse(@Context ContainerRequestContext context,
                              @DefaultValue("0") @QueryParam("tag_id") int tagId,
                              @DefaultValue("ENABLED") @QueryParam("status") String status,
                              @DefaultValue("10") @QueryParam("number") int number,
                              @DefaultValue("0") @QueryParam("page_index") int pageIndex,
                              @DefaultValue("0") @QueryParam("ignore_course_id") int ignoreCourseId) {
        logger.info("get course " + tagId);
        WeChatUserInfo userInfo = (WeChatUserInfo) context.getProperty(ContextKeys.WECHAT_USER);
        List<CourseQueryBean> beans = courseProposalService.queryCourses(userInfo, tagId, status, number, pageIndex, ignoreCourseId);
        return Response.ok(beans).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/query_by_date")
    public Response getCourseByDate(@Context HttpServletRequest request,
                                    @Context ContainerRequestContext context,
                                    @DefaultValue("0") @QueryParam("tag_id") int tagId,
                                    @DefaultValue("ENABLED") @QueryParam("status") String status,
                                    @DefaultValue("10") @QueryParam("number") int number,
                                    @DefaultValue("0") @QueryParam("page_index") int pageIndex) {
        logger.info("get course " + tagId);
        WeChatUserInfo userInfo = (WeChatUserInfo) context.getProperty(ContextKeys.WECHAT_USER);
        Map<String, List<CourseQueryBean>> beans = courseProposalService.queryCourseByDate(userInfo, tagId, status, number, pageIndex);
        return Response.ok(beans).build();
    }

    @GET
    @Path("/{course_id}")
    @Produces(MediaType.APPLICATION_JSON)
    @TokenAccess
    public Response getCourse(@Context HttpServletRequest request,
                              @Context ContainerRequestContext context,
                              @PathParam("course_id") int courseId) {
        String token = (String) context.getProperty(HeaderKeys.ACCESS_TOKEN);
        UserEntity userInfo = null;
        if (token != null) {
            logger.info("login user " + token);
            userInfo = historyService.getUserByToken(token);
        } else {
            logger.info("anonymous query ");
        }
        CourseQueryBean b = courseProposalService.queryCourse(userInfo, courseId);

        return Response.ok(b).build();
    }

    @GET
    @Path("/count")
    @Produces(MediaType.TEXT_PLAIN)
    public Response getCourseCount(@DefaultValue("0") @QueryParam("course_tag_id") int tagId){
        long count = courseProposalService.getCourseCount(tagId);
        logger.info("get course count "+count+" for tag "+tagId);
        return Response.ok(count).build();

    }
}
