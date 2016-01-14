package com.education.service;

import com.education.formbean.HomeConfigResp;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import org.glassfish.jersey.internal.util.collection.ByteBufferInputStream;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.StringBufferInputStream;
import java.util.List;

/**
 * Created by yzzhao on 1/14/16.
 */
@Transactional
public class HomeConfigServiceTest extends AbstractServiceTest {

    @Autowired
    private HomeConfigService configService;

    @Test
    @DatabaseSetup(value="classpath:/com/education/service/homeconfig_service_test.xml")
    public void testGetHomeConfigImage() {
        List<HomeConfigResp> homeImages = configService.getHomeImages();
        Assert.assertEquals(4, homeImages.size());
        for(HomeConfigResp resp: homeImages){
            Assert.assertEquals(2000, resp.getCourseId());
        }
    }
}
