package com.education.jerseytest;

import com.education.ws.CourseRegisterService;
import com.education.ws.ReservationCourseService;
import org.glassfish.jersey.server.ResourceConfig;
import org.junit.Assert;
import org.junit.Test;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.Response;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by yzzhao on 11/12/15.
 */
public class ReserveCourseServiceTest extends AbstractJerseyTest {
    @Override
    protected Application configure() {
        return new ResourceConfig(ReservationCourseService.class);
    }

    @Test
    public void testMakeReserve(){
        Form form = new Form();
        form.param("mobile","1234");
        form.param("name","aaaa");
        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        form.param("date", format.format(date));
        form.param("child_birthdate", format.format(date));
        Response response = target("/reservation").request().post(Entity.form(form));
        Assert.assertEquals(200, response.getStatus());

        response = target("/reservation").request().get();
        List<String> list = response.readEntity(List.class);
        Assert.assertTrue(list.size()>0);
    }

}
