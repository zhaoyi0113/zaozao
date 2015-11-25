package com.education.ws;

import com.education.db.entity.CoursePlanEntity;
import com.education.db.jpa.CoursePlanRepository;
import com.education.formbean.CoursePlanBean;
import com.google.gson.Gson;
import jersey.repackaged.com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;

/**
 * Created by yzzhao on 11/25/15.
 */
@Path("courseplan")
public class CoursePlanService {

    @Autowired
    private CoursePlanRepository coursePlanRepository;

    @GET
    public Response getAllCoursePlans(){
        Iterable<CoursePlanEntity> iterable = coursePlanRepository.findAll();
        ArrayList<CoursePlanEntity> list = Lists.newArrayList(iterable);
        Gson gson = new Gson();
        String json = gson.toJson(list);
        return Response.ok(json).build();
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response createNewCoursePlan(@BeanParam CoursePlanBean bean){
        CoursePlanEntity entity = new CoursePlanEntity(bean);
        CoursePlanEntity saved = coursePlanRepository.save(entity);

        return Response.ok(saved).build();
    }
}
