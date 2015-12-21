package com.education.ws;


import com.education.auth.Public;
import com.education.service.CourseProposalService;
import com.education.service.WeChatUserInfo;
import com.education.ws.util.ContextKeys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("course/proposal")
@EnableTransactionManagement
@Transactional
@Service
public class CourseProposalAPI {

    @Autowired
    private CourseProposalService courseProposalService;

    @GET
    @Public(requireWeChatCode = true)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getProposalCourse(ContainerRequestContext context){
        WeChatUserInfo userInfo = (WeChatUserInfo) context.getProperty(ContextKeys.WECHAT_USER);
        List<CourseRegisterBean> courseRegisterBeans = courseProposalService.proposeCourse(userInfo);
        return Response.ok(courseRegisterBeans).build();
    }

}
