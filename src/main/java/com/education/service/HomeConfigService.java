package com.education.service;

import com.education.db.entity.HomeConfigEntity;
import com.education.db.jpa.HomeConfigRepository;
import com.education.formbean.HomeConfigResp;
import com.education.ws.util.WSUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by yzzhao on 12/25/15.
 */
@Service("HomeConfigService")
public class HomeConfigService {

    @Autowired
    private HomeConfigRepository homeConfigRepository;


    @Value("#{config['home_image_path']}")
    private String homeImagePath;

    @Value("#{config['home_image_url']}")
    private String homeImageUrl;

    @Autowired
    private WSUtility wsUtility;

    @Transactional
    public void createImage(String fileName, InputStream inputStream){
        HomeConfigEntity entity = new HomeConfigEntity();
        entity.setImage(fileName);
        homeConfigRepository.save(entity);
        wsUtility.writeFile(inputStream, homeImagePath, fileName);
    }

    public List<HomeConfigResp> getHomeImages(){
        List<HomeConfigResp> imageUrls = new ArrayList<>();
        Iterable<HomeConfigEntity> entities = homeConfigRepository.findAll();
        for(HomeConfigEntity entity : entities){
            imageUrls.add(new HomeConfigResp(entity.getId(), homeImageUrl+"/"+entity.getImage(), entity.getImage()));
        }
        return imageUrls;
    }

    @Transactional
    public void deleteImage(int id) {
        homeConfigRepository.delete(id);
    }
}
