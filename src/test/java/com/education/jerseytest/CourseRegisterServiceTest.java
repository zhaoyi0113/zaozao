package com.education.jerseytest;

import com.education.db.DBConnection;
import com.education.db.DBUtil;
import com.education.db.entity.CourseEntity;
import com.education.ws.CourseRegisterService;
import com.google.gson.Gson;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.hibernate.Session;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import javax.ws.rs.client.Entity;

import javax.ws.rs.core.Application;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by yzzhao on 11/3/15.
 */
public class CourseRegisterServiceTest extends AbstractJerseyTest {
    @Before
    public void before() {
        clearTable("course");
    }

    @Override
    protected Application configure() {

        return new ResourceConfig(CourseRegisterService.class);

    }

    @Test
    public void testNewCourse(){
        Form form = new Form();
        form.param("name","张三");
        form.param("content", "test");
        form.param("category", "父母学院");
        form.param("date", "2015-10-10");

        Response response = target("course/new").request().
                post(Entity.form(form));
        Assert.assertEquals(200, response.getStatus());
        response = target("course/queryall").request().get();
        String entity =response.readEntity(String.class);
        Gson gson = new Gson();
        List list = gson.fromJson(entity, List.class);
        Assert.assertEquals(1, list.size());
        Map<String,String> course = (Map) list.get(0);
        Assert.assertTrue("张三".equals(course.get("name")));
        Assert.assertTrue("test".equals(course.get("content")));
        Assert.assertTrue("父母学院".equals(course.get("category")));
        Assert.assertTrue("2015-10-10".equals(course.get("date")));

        response = target("course/allnames").request().get();
        entity = response.readEntity(String.class);

    }

    @Test
    public void testDateFormat(){
        String str = "2015-November-Wednesday";
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-DD");
        try {
            Date date = format.parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }
}
