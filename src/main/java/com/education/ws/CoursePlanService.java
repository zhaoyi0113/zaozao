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
        return Response.ok(json).header("Access-Control-Allow-Origin","*")
                .header("Access-Control-Allow-Methods","*").build();
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response createNewCoursePlan(@BeanParam CoursePlanBean bean){
        CoursePlanEntity entity = new CoursePlanEntity(bean);
        CoursePlanEntity saved = coursePlanRepository.save(entity);

        return Response.ok(saved).build();
    }

    @POST
    @Path("/edit")
    @Produces(MediaType.APPLICATION_JSON)
    public Response editCoursePlan(@BeanParam CoursePlanBean bean){
        CoursePlanEntity entity = coursePlanRepository.findOne(bean.getId());
        if(entity != null){
            entity.updateValues(bean);
            coursePlanRepository.save(entity);
            return Response.ok(bean).build();
        }else{
            throw new BadRequestException("Can't find course plan "+bean.getId()+", "+bean.getTitle());
        }

    }

    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    @Path("{id}")
    public Response removeCoursePlan(@PathParam("id") int id){
        CoursePlanEntity entity = coursePlanRepository.findOne(id);
        if(entity != null){
            coursePlanRepository.delete(entity);
            return Response.ok().build();
        }
        throw new BadRequestException("Can't find course plan "+id);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("{id}")
    public Response findCoursePlan(@PathParam("id") int id){
        CoursePlanEntity entity = coursePlanRepository.findOne(id);
        if(entity != null){
            return Response.ok(entity).build();
        }
        throw new BadRequestException("Can't find course plan "+id);
    }
}
