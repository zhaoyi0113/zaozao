package com.education.service;

import com.education.db.entity.CourseEntity;
import com.education.db.entity.HomeConfigEntity;
import com.education.db.jpa.CourseRepository;
import com.education.db.jpa.HomeConfigRepository;
import com.education.formbean.HomeConfigResp;
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
    public void createImage(String fileName,int courseId, InputStream inputStream){
        HomeConfigEntity entity = new HomeConfigEntity();
        entity.setImage(fileName);
        entity.setCourseId(courseId);
        homeConfigRepository.save(entity);
        wsUtility.writeFile(inputStream, homeImagePath, fileName);
    }

    public List<HomeConfigResp> getHomeImages(){
        List<HomeConfigResp> imageUrls = new ArrayList<>();
        Iterable<HomeConfigEntity> entities = homeConfigRepository.findAll();
        for(HomeConfigEntity entity : entities){
            HomeConfigResp config = new HomeConfigResp(entity.getId(), homeImageUrl + "/" + entity.getImage(), entity.getImage(), entity.getCourseId());
            CourseEntity course = courseRepository.findOne(entity.getCourseId());
            if(course != null){
                config.setCourseName(course.getName());
            }
            imageUrls.add(config);
        }
        return imageUrls;
    }

    @Transactional
    public void deleteImage(int id) {
        HomeConfigEntity config = homeConfigRepository.findOne(id);
        if(config != null) {
            homeConfigRepository.delete(id);
            String path = homeImagePath+"/"+config.getImage();
            logger.info("delete home config file "+path);
            File file = new File(path);
            file.delete();
        }
    }
}
