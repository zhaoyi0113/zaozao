package com.education.ws;


import com.education.auth.Public;
import com.education.service.CourseProposalService;
import com.education.service.WeChatUserInfo;
import com.education.ws.util.ContextKeys;
import com.education.ws.util.WSUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

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

    @GET
    @Public(requireWeChatCode = false)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/query")
    public Response getCourse(@Context ContainerRequestContext context,
                              @DefaultValue("0") @QueryParam("tag_id") int tagId,
                              @DefaultValue("ENABLED") @QueryParam("status") String status,
                              @DefaultValue("3") @QueryParam("number") int number) {
        logger.info("get course " + tagId);
        WeChatUserInfo userInfo = (WeChatUserInfo) context.getProperty(ContextKeys.WECHAT_USER);
        List<CourseRegisterBean> beans = courseProposalService.queryCourse(userInfo, tagId, status, number);
        return WSUtility.buildResponse(beans);
    }

    @GET
    @Public(requireWeChatCode = false)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/query_by_date")
    public Response getCourseByDate(@Context ContainerRequestContext context,
                                    @DefaultValue("0") @QueryParam("tag_id") int tagId,
                                    @DefaultValue("ENABLED") @QueryParam("status") String status,
                                    @DefaultValue("10") @QueryParam("number") int number) {
        logger.info("get course " + tagId);
        WeChatUserInfo userInfo = (WeChatUserInfo) context.getProperty(ContextKeys.WECHAT_USER);
        Map<String, List<CourseRegisterBean>> beans = courseProposalService.queryCourseByDate(userInfo, tagId, status, number);
        return WSUtility.buildResponse(beans);
    }

}
