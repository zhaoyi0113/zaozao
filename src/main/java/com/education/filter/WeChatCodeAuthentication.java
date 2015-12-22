package com.education.filter;

import com.education.auth.Public;
import com.education.db.entity.UserEntity;
import com.education.db.jpa.UserRepository;
import com.education.exception.BadRequestException;
import com.education.exception.ErrorCode;
import com.education.service.LoginHistoryService;
import com.education.service.WeChatService;
import com.education.service.WeChatUserInfo;
import com.education.ws.util.ContextKeys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

/**
 * Created by yzzhao on 12/16/15.
 */
@Provider
public class WeChatCodeAuthentication implements ContainerRequestFilter {

    private static final Logger logger = Logger.getLogger(WeChatCodeAuthentication.class.getName());

    @Context
    private ResourceInfo resourceInfo;

    @Autowired
    private WeChatService weChatService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LoginHistoryService loginHistoryService;

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        Public annotation = resourceInfo.getResourceMethod().getAnnotation(Public.class);
        if (annotation == null || !annotation.requireWeChatCode()) {
            return;
        }
        String code = getWeChatCode(requestContext);
        logger.info("get wechat code " + code);
        WeChatUserInfo webUserInfo = weChatService.getWebUserInfo(code);
        if (webUserInfo.getErrcode() != null) {
            throw new BadRequestException(Response.Status.BAD_REQUEST);
        }
        if (annotation.requireWeChatUser()) {
            checkUserExistent(webUserInfo);
        }
        int userId = registerUserIfNotExist(webUserInfo);
        loginHistoryService.saveLogin(userId);
        requestContext.setProperty(ContextKeys.WECHAT_USER, webUserInfo);
    }

    private String getWeChatCode(ContainerRequestContext requestContext) {
        MultivaluedMap<String, String> pathParameters = requestContext.getUriInfo().getQueryParameters();
        if (pathParameters == null) {
            throw new BadRequestException(ErrorCode.WECHAT_CODE_ERROR);
        }
        logger.info("request context parameters " + pathParameters);
        List<String> code = pathParameters.get("code");
        if (code == null || code.isEmpty()) {
            logger.severe(ErrorCode.WECHAT_CODE_ERROR.getEnMsg());
            throw new BadRequestException(ErrorCode.WECHAT_CODE_ERROR);
        }
        return code.get(0);
    }

    private void checkUserExistent(WeChatUserInfo userInfo) {
        List<UserEntity> entity = userRepository.findByOpenid(userInfo.getOpenId());
        if (entity == null || entity.size()<=0) {
            throw new BadRequestException(ErrorCode.USER_NOT_EXISTED);
        }
    }

    @Transactional
    private int registerUserIfNotExist(WeChatUserInfo userInfo){
        UserEntity entity = new UserEntity();
        entity.setGroupid(userInfo.getGroupid());
        entity.setRemark(userInfo.getRemark());
        entity.setCity(userInfo.getCity());
        entity.setCountry(userInfo.getCountry());
        entity.setLanguage(userInfo.getLanguage());
        entity.setHeadimageurl(userInfo.getHeadimgurl());
        entity.setOpenid(userInfo.getOpenId());
        entity.setSex(userInfo.getSex());
        entity.setNickname(userInfo.getNickname());
        entity.setUserName(userInfo.getNickname());
        entity.setProvince(userInfo.getProvince());
        entity.setSubscribe(userInfo.getSubscribe());
        entity.setUnionid(userInfo.getUnionid());
        entity.setSubscribe_time(userInfo.getSubscribe_time());
        UserEntity saved = userRepository.save(entity);
        return saved.getUserId();
    }
}
