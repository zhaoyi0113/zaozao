package com.education.ws;


import com.education.auth.Public;
import com.education.service.CourseProposalService;
import com.education.service.WeChatUserInfo;
import com.education.ws.util.ContextKeys;
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
                              @DefaultValue("0") @QueryParam("category") int category,
                              @DefaultValue("1") @QueryParam("status") String status) {
        logger.info("get course " + category);
        WeChatUserInfo userInfo = (WeChatUserInfo) context.getProperty(ContextKeys.WECHAT_USER);
        List<CourseRegisterBean> beans = courseProposalService.queryCourse(userInfo, category, status);
        return Response.ok(beans).build();
    }


}
