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
public class CoursePlanServiceTest extends JerseyTest{

    @Override
    protected Application configure() {
        ResourceConfig config = new ResourceConfig();
        config.register(CoursePlanService.class);
        return config;
    }

    @Test
    public void testGetAllCoursePlans(){
        Form form = new Form();
        form.param("title", "test1");
        form.param("sub_title", "sub1");
        form.param("content", "content");
        form.param("price", "400.5");
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
}
