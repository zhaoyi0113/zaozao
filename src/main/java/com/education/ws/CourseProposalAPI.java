package com.education.ws;


import com.education.auth.Public;
import com.education.formbean.CourseQueryBean;
import com.education.service.CourseProposalService;
import com.education.service.CourseService;
import com.education.service.WeChatUserInfo;
import com.education.ws.util.ContextKeys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
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

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/query")
    public Response getCourse(@Context ContainerRequestContext context,
                              @DefaultValue("0") @QueryParam("tag_id") int tagId,
                              @DefaultValue("ENABLED") @QueryParam("status") String status,
                              @DefaultValue("10") @QueryParam("number") int number,
                              @DefaultValue("0") @QueryParam("page_index") int pageIndex) {
        logger.info("get course " + tagId);
        WeChatUserInfo userInfo = (WeChatUserInfo) context.getProperty(ContextKeys.WECHAT_USER);
        List<CourseQueryBean> beans = courseProposalService.queryCourses(userInfo, tagId, status, number, pageIndex);
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
    public Response getCourse(@Context HttpServletRequest request,
                              @Context ContainerRequestContext context,
                              @PathParam("course_id") int courseId){
        WeChatUserInfo userInfo = null;
        HttpSession session = request.getSession();
        if(session.getAttribute(ContextKeys.WECHAT_USER) != null){
            userInfo = (WeChatUserInfo) session.getAttribute(ContextKeys.WECHAT_USER);
            logger.info("login user "+userInfo.getUnionid());
        }else{
            logger.info("anonymous query "+session.getId());
        }
        CourseQueryBean b = courseProposalService.queryCourse(userInfo,courseId);

        return Response.ok(b).build();
    }
}
