package com.education.service;

import com.education.db.entity.CourseEntity;
import com.education.db.entity.HomeConfigEntity;
import com.education.db.jpa.CourseRepository;
import com.education.db.jpa.HomeConfigRepository;
import com.education.exception.BadRequestException;
import com.education.exception.ErrorCode;
import com.education.formbean.HomeConfigResp;
import com.education.util.MoveAction;
import com.education.ws.util.WSUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Created by yzzhao on 12/25/15.
 */
@Service("HomeConfigService")
public class HomeConfigService {

    private static final Logger logger = Logger.getLogger(HomeConfigService.class.getName());

    @Autowired
    private HomeConfigRepository homeConfigRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Value("#{config['home_image_path']}")
    private String homeImagePath;

    @Value("#{config['home_image_url']}")
    private String homeImageUrl;

    @Autowired
    private WSUtility wsUtility;

    @Transactional
    public int createImage(String fileName, int courseId, InputStream inputStream) {
        HomeConfigEntity entity = new HomeConfigEntity();
        entity.setImage(fileName);
        entity.setCourseId(courseId);
        HomeConfigEntity top = homeConfigRepository.findTopByOrderByOrderIndexDesc();
        if (top != null) {
            entity.setOrderIndex(top.getOrderIndex() + 1);
        } else {
            entity.setOrderIndex(0);
        }
        HomeConfigEntity saved = homeConfigRepository.save(entity);
        wsUtility.writeFile(inputStream, homeImagePath, fileName);
        return saved.getId();
    }

    public List<HomeConfigResp> getHomeImages() {
        List<HomeConfigResp> imageUrls = new ArrayList<>();
        Iterable<HomeConfigEntity> entities = homeConfigRepository.findOrderByOrderIndex();
        for (HomeConfigEntity entity : entities) {
            HomeConfigResp config = new HomeConfigResp(entity.getId(), homeImageUrl + "/" + entity.getImage(), entity.getImage(), entity.getCourseId());
            CourseEntity course = courseRepository.findOne(entity.getCourseId());
            if (course != null) {
                config.setCourseName(course.getName());
            }
            imageUrls.add(config);
        }
        return imageUrls;
    }

    @Transactional
    public void moveUp(int id) {
        HomeConfigEntity config = homeConfigRepository.findOne(id);
        if (config == null) {
            throw new BadRequestException(ErrorCode.COMMON_NOT_FOUND);
        }
        List<HomeConfigEntity> configBefore = homeConfigRepository.findByOrderIndexLessThanOrderByOrderIndex(config.getOrderIndex());
        if (configBefore.size() > 0) {
            HomeConfigEntity last = configBefore.get(configBefore.size() - 1);
            int order = config.getOrderIndex();
            config.setOrderIndex(last.getOrderIndex());
            last.setOrderIndex(order);
            homeConfigRepository.save(last);
            homeConfigRepository.save(config);
        }
    }

    @Transactional
    public void moveDown(int id) {
        logger.info("move down " + id);
        HomeConfigEntity config = homeConfigRepository.findOne(id);
        if (config == null) {
            throw new BadRequestException(ErrorCode.COMMON_NOT_FOUND);
        }
        List<HomeConfigEntity> items = homeConfigRepository.findByOrderIndexGreaterThanOrderByOrderIndex(config.getOrderIndex());
        if (items.size() > 0) {
            HomeConfigEntity item = items.get(0);
            int order = item.getOrderIndex();
            item.setOrderIndex(config.getOrderIndex());
            config.setOrderIndex(order);
            homeConfigRepository.save(item);
            homeConfigRepository.save(config);
        }
    }

    public void moveAction(int id, String action) {
        MoveAction moveAction = MoveAction.valueOf(action);
        switch (moveAction) {
            case UP:
                moveUp(id);
                break;
            case DOWN:
                moveDown(id);
                break;
        }

    }

    @Transactional
    public void deleteImage(int id) {
        HomeConfigEntity config = homeConfigRepository.findOne(id);
        if (config != null) {
            homeConfigRepository.delete(id);
            String path = homeImagePath + "/" + config.getImage();
            logger.info("delete home config file " + path);
            File file = new File(path);
            file.delete();
            reorder();
        }
    }

    private void reorder(){
        Iterable<HomeConfigEntity> entities = homeConfigRepository.findOrderByOrderIndex();
        int index=0;
        for(HomeConfigEntity entity: entities){
            entity.setOrderIndex(index);
            index++;
        }
    }
}
