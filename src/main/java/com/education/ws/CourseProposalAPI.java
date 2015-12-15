package com.education.ws;


import com.education.service.CourseProposalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

@Path("course/proposal")
@EnableTransactionManagement
@Transactional
@Service
public class CourseProposalAPI {

    @Autowired
    private CourseProposalService courseProposalService;

    @GET
    public Response getProposalCourse(){

        return Response.ok().build();
    }

}
