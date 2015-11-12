package com.education.ws;

import com.education.db.entity.ReserveCourseEntity;
import com.education.db.jpa.ReserveCourseRepository;
import jersey.repackaged.com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.logging.Logger;

/**
 * Created by yzzhao on 11/12/15.
 */
@Path("reservation")
@Component
public class ReservationCourseService {

    private static final Logger LOGGER = Logger.getLogger(ReservationCourseService.class.getName());

    @Autowired
    private ReserveCourseRepository reserveRepository;

    @POST
    public Response reserveCourse(@BeanParam ReserveCourseBean bean){
        LOGGER.info("reserve course "+bean.getMobile());
        ReserveCourseEntity reserveCourseEntity = new ReserveCourseEntity(bean);
        reserveRepository.save(reserveCourseEntity);
        return Response.ok().build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllReservedCourses(){
        LOGGER.info("get all reserved course");
        Iterable<ReserveCourseEntity> allCourses = reserveRepository.findAll();
        ArrayList<ReserveCourseEntity> list = Lists.newArrayList(allCourses);

        return Response.ok(list).build();
    }



}
