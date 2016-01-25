package com.education.service;

import com.education.auth.WeChatAccessState;
import com.education.db.entity.ChildrenEntity;
import com.education.db.entity.UserEntity;
import com.education.db.jpa.ChildrenRepository;
import com.education.db.jpa.UserRepository;
import com.education.exception.BadRequestException;
import com.education.exception.ErrorCode;
import com.education.formbean.UserChildrenRegisterBean;
import com.education.scheduler.AccessTokenScheduler;
import com.education.service.converter.ChildrenRegisterBeanConverter;
import com.education.ws.util.WSUtility;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by yzzhao on 12/15/15.
 */
@Service("ParentService")
public class ParentService {

    private static final Logger logger = Logger.getLogger(ParentService.class.getName());

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ChildrenRepository childrenRepository;

    @Autowired
    private ChildrenRegisterBeanConverter converter;

    @Autowired
    private AccessTokenScheduler tokenScheduler;

    @Autowired
    private WSUtility wsUtility;

    @Value("#{config['wechat_download_media_uri']}")
    private String mediaDownloadUri;

    @Value("#{config['user_head_image']}")
    private String userHeadImageDir;

    @Value("#{config['user_head_url']}")
    private String userHeadImageUrl;

    private int addUserChild(String userName, UserChildrenRegisterBean bean, UserEntity userEntity) {
        if (userName != null) {
            userEntity.setUserName(userName);
            userEntity.setNickname(userName);
            if(bean.getMediaId() !=null){
                String fileName = downMediaFromWeChat(bean.getMediaId(), userEntity.getUserId());
                userEntity.setHeadimageurl(fileName);
            }
            userRepository.save(userEntity);
        }
        ChildrenEntity childrenEntity = createChildrenEntity(bean, userEntity.getUserId());
        ChildrenEntity save = childrenRepository.save(childrenEntity);
        return save.getId();
    }

    @Transactional
    public void updateUserProfile(String userName, UserChildrenRegisterBean bean, int userId) {
        UserEntity userEntity = userRepository.findOne(userId);
        if (userEntity == null) {
            throw new BadRequestException(ErrorCode.NOT_LOGIN);
        }
        List<ChildrenEntity> children = childrenRepository.findByParentId(userEntity.getUserId());
        if (children.isEmpty()) {
            addUserChild(userName, bean, userEntity);
            return;
        }
        ChildrenEntity entity = children.get(0);

        if (userName != null) {
            userEntity.setUserName(userName);
            userEntity.setNickname(userName);
            userRepository.save(userEntity);
        }
        entity.setAge(bean.getAge());
        entity.setGender(bean.getGender());
        entity.setBirthdate(WSUtility.stringToDate(bean.getChildBirthdate()));
        entity.setName(bean.getChildName());
        if(bean.getMediaId() != null){
            String fileName = downMediaFromWeChat(bean.getMediaId(), userId);
            userEntity.setHeadimageurl(fileName);
            userRepository.save(userEntity);
        }

        childrenRepository.save(entity);
    }

    private String downMediaFromWeChat(String mediaId, int userId) {
        // test media-id = 6llm4VWwa_UVEk5WrVoaNcAlh7_VF7KCq3518YLx211u1ViFL7ZnHTdS5Syre_by
        if (mediaId == null) {
            mediaId = "6llm4VWwa_UVEk5WrVoaNcAlh7_VF7KCq3518YLx211u1ViFL7ZnHTdS5Syre_by";
        }
        String accessToken = tokenScheduler.getAccessToken(WeChatAccessState.WECHAT_SERVICE);
        if (accessToken == null) {
            return null;
        }
        String uri = mediaDownloadUri + "?access_token=" + accessToken + "&media_id=" + mediaId;
        HttpGet httpGet = new HttpGet(uri);
        HttpClient httpClient = HttpClients.createDefault();
        try {
            HttpResponse response = httpClient.execute(httpGet);
            HttpEntity entity = response.getEntity();
            InputStream inputStream = entity.getContent();
            String dir = userHeadImageDir;
            wsUtility.writeFile(inputStream, dir, String.valueOf(userId));
            return userHeadImageUrl+"/"+userId;
        } catch (IOException e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
        }
        return null;
    }

    @Transactional
    public void deleteUserChild(int userId) {
        UserEntity userEntity = userRepository.findOne(userId);
        if (userEntity == null) {
            throw new BadRequestException(ErrorCode.NOT_LOGIN);
        }
        childrenRepository.deleteByParentId(userEntity.getUserId());
    }

    public UserChildrenRegisterBean getUserChild(int userId) {
        List<ChildrenEntity> children = childrenRepository.findByParentId(userId);
        if (children.isEmpty()) {
            return null;
        }
        UserChildrenRegisterBean bean = converter.convert(children.get(0));
        return bean;
    }

    private ChildrenEntity createChildrenEntity(UserChildrenRegisterBean bean, int parentId) {
        ChildrenEntity entity = new ChildrenEntity();
        entity.setName(bean.getChildName());
        Date date = WSUtility.stringToDate(bean.getChildBirthdate());
        entity.setBirthdate(date);
        entity.setGender(bean.getGender());
        entity.setParentId(parentId);
        entity.setAge(bean.getAge());
        return entity;
    }

}
