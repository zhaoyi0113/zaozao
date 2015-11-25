package com.education.jerseytest;

import com.education.db.entity.CoursePlanEntity;
import com.education.ws.CoursePlanService;
import com.github.springtestdbunit.TransactionDbUnitTestExecutionListener;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

/**
 * Created by yzzhao on 11/25/15.
 */

@ContextConfiguration(locations = {"classpath*:applicationContext.xml"})
@RunWith(SpringJUnit4ClassRunner.class)
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = true)
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class,
        DirtiesContextTestExecutionListener.class,
        TransactionDbUnitTestExecutionListener.class})
@Transactional
public class CoursePlanServiceTest extends JerseyTest {

    @Override
    protected Application configure() {
        ResourceConfig config = new ResourceConfig();
        config.register(CoursePlanService.class);
        return config;
    }

    @Test
    public void testGetAllCoursePlans() {
        Form form = createNewCourseForm();
        Response response = target("courseplan").request().post(Entity.form(form));
        Assert.assertEquals(200, response.getStatus());
        CoursePlanEntity entity = response.readEntity(CoursePlanEntity.class);
        Assert.assertNotNull(entity);
        Assert.assertEquals("test1", entity.getTitle());
        Assert.assertEquals("sub1", entity.getSubTitle());
        Assert.assertEquals(400.5, entity.getPrice(), 0.05);

        response = target("courseplan").request().get();
        Assert.assertEquals(200, response.getStatus());


    }

    private Form createNewCourseForm() {
        Form form = new Form();
        form.param("title", "test1");
        form.param("sub_title", "sub1");
        form.param("content", "content");
        form.param("price", "400.5");
        return form;
    }

    @Test
    public void testDeleteCoursePlan() {
        Form form = createNewCourseForm();
        Response response = target("courseplan").request().post(Entity.form(form));
        Assert.assertEquals(200, response.getStatus());
        CoursePlanEntity entity = response.readEntity(CoursePlanEntity.class);
        response = target("courseplan/" + entity.getId()).request().get();
        entity = response.readEntity(CoursePlanEntity.class);
        Assert.assertNotNull(entity);


        response = target("courseplan/" + entity.getId()).request().delete();
        Assert.assertEquals(200, response.getStatus());
        response = target("courseplan/" + entity.getId()).request().get();
        Assert.assertEquals(400, response.getStatus());
    }

    @Test
    public void testEditCoursePlan() {
        Form form = createNewCourseForm();
        Response response = target("courseplan").request().post(Entity.form(form));
        Assert.assertEquals(200, response.getStatus());
        CoursePlanEntity entity = response.readEntity(CoursePlanEntity.class);

        Form newform = new Form();
        newform.param("title", "newtitle");
        newform.param("sub_title", "sub1");
        newform.param("content", "content");
        newform.param("price", "400.5");
        newform.param("id", entity.getId()+"");

        response = target("courseplan/edit").request().post(Entity.form(newform));
        Assert.assertEquals(200, response.getStatus());
        entity = response.readEntity(CoursePlanEntity.class);
        Assert.assertEquals("newtitle", entity.getTitle());
    }
}
