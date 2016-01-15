package com.education.service;

import com.education.db.entity.HomeConfigEntity;
import com.education.db.jpa.HomeConfigRepository;
import com.education.formbean.HomeConfigResp;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import org.glassfish.jersey.internal.util.collection.ByteBufferInputStream;
import org.junit.Assert;
import org.junit.Ignore;
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

    @Autowired
    private HomeConfigRepository configRepository;

    @Test
    @DatabaseSetup(value = "classpath:/com/education/service/homeconfig_service_test.xml")
    public void testGetHomeConfigImage() {
        List<HomeConfigResp> homeImages = configService.getHomeImages();
        Assert.assertEquals(4, homeImages.size());
        for (HomeConfigResp resp : homeImages) {
            Assert.assertEquals(2000, resp.getCourseId());
            Assert.assertEquals("c1", resp.getCourseName());
        }
    }


    @Test
    @DatabaseSetup(value = "classpath:/com/education/service/homeconfig_service_test.xml")
    public void testMoveUpDown() {
        configService.moveUp(10001);
        List<HomeConfigResp> homeImages = configService.getHomeImages();
        testOrder(homeImages, new int[]{10001, 10000, 10002, 10003});
        configService.moveUp(10002);
        testOrder(configService.getHomeImages(), new int[]{10001, 10002, 10000, 10003});
        configService.moveUp(10003);
        testOrder(configService.getHomeImages(), new int[]{10001, 10002, 10003, 10000});
        configService.moveUp(10001);
        testOrder(configService.getHomeImages(), new int[]{10001, 10002, 10003, 10000});
        configService.moveDown(10001);
        testOrder(configService.getHomeImages(), new int[]{10002, 10001, 10003, 10000});
        configService.moveDown(10001);
        testOrder(configService.getHomeImages(), new int[]{10002, 10003, 10001, 10000});
        configService.moveDown(10001);
        testOrder(configService.getHomeImages(), new int[]{10002, 10003, 10000, 10001});
        configService.moveDown(10001);
        testOrder(configService.getHomeImages(), new int[]{10002, 10003, 10000, 10001});
    }

    @Test
    @DatabaseSetup(value = "classpath:/com/education/service/homeconfig_service_test.xml")
    public void testCreateHomeConfig() {
        int id = configService.createImage("aaaa", 2000, null);
        HomeConfigEntity config = configRepository.findOne(id);
        Assert.assertEquals(4, config.getOrderIndex());
        configService.moveDown(10003);
        testOrder(configService.getHomeImages(), new int[]{10000, 10001, 10002, id, 10003});

    }

    @Test
    @DatabaseSetup(value = "classpath:/com/education/service/homeconfig_service_test.xml")
    public void testDeleteHomeConfig() {
        configRepository.delete(10002);
        testOrder(configService.getHomeImages(), new int[]{10000, 10001, 10003});

    }

    private void testOrder(List<HomeConfigResp> items, int[] order) {
        for (int i = 0; i < order.length; i++) {
            Assert.assertEquals(order[i], items.get(i).getId());
        }
    }

}
