package com.education.jerseytest;

import com.education.db.entity.CourseTypeEntity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import com.education.ws.CourseTypeService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import javax.ws.rs.core.Application;
import javax.ws.rs.core.Response;
import java.util.List;

/**
 * Created by yzzhao on 11/6/15.
 */
@Ignore
public class CourseTypeServiceTest extends AbstractJerseyTest{

    @Override
    protected Application configure() {
        return new ResourceConfig(CourseTypeService.class);
    }

    @Test
    public void testGetCourseType(){
        Response response = target("coursetype").request().get();
        Gson gson = new Gson();
        String str = response.readEntity(String.class);
        List<CourseTypeEntity> list = gson.fromJson(str, new TypeToken<List<CourseTypeEntity>>(){}.getType());
        Assert.assertEquals(5, list.size());

        for(int i=0;i<list.size(); i++){
            CourseTypeEntity entity = list.get(i);
            Assert.assertEquals(i+1, entity.getId());
        }
    }

}
