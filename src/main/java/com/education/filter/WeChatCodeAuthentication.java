package com.education.filter;

import com.education.auth.Public;
import com.education.db.entity.UserEntity;
import com.education.db.jpa.UserRepository;
import com.education.exception.BadRequestException;
import com.education.exception.ErrorCode;
import com.education.service.WeChatService;
import com.education.service.WeChatUserInfo;
import com.education.service.converter.UserEntityConverter;
import com.education.ws.util.ContextKeys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MultivaluedMap;
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
    private UserEntityConverter userEntityConverter;

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        Public annotation = resourceInfo.getResourceMethod().getAnnotation(Public.class);
        if (annotation == null) {
            return;
        }
        String code = getWeChatCode(requestContext, annotation);
        String status = getWeChatState(requestContext);
        logger.info("get wechat code " + code+", state="+status);
        WeChatUserInfo webUserInfo = weChatService.getWebUserInfo(code, status);
        if (webUserInfo == null) {
            logger.severe("not able to find wechat user info");
            return;
        }
        if (webUserInfo.getErrcode() != null || webUserInfo.getOpenid() == null) {
            //throw new BadRequestException(Response.Status.BAD_REQUEST);
            logger.severe("can't get wechat user info," + webUserInfo.getErrcode() + "," + webUserInfo.getOpenid());
            return;
        }
        if (annotation.requireWeChatUser()) {
            checkUserExistent(webUserInfo);
        }
        registerUserIfNotExist(webUserInfo);
        requestContext.setProperty(ContextKeys.WECHAT_USER, webUserInfo);
    }

    private String getWeChatCode(ContainerRequestContext requestContext, Public annotation) {
        MultivaluedMap<String, String> pathParameters = requestContext.getUriInfo().getQueryParameters();
        if (pathParameters == null) {
            if (annotation.requireWeChatCode() == false) {
                return null;
            }
            throw new BadRequestException(ErrorCode.WECHAT_CODE_ERROR);
        }
        logger.info("request context parameters " + pathParameters);
        List<String> code = pathParameters.get("code");
        if ((code == null || code.isEmpty())) {
            if (annotation.requireWeChatCode() == false) {
                return null;
            }
            logger.severe(ErrorCode.WECHAT_CODE_ERROR.getEnMsg());
            throw new BadRequestException(ErrorCode.WECHAT_CODE_ERROR);
        }
        return code.get(0);
    }

    private String getWeChatState(ContainerRequestContext requestContext){
        MultivaluedMap<String, String> pathParameters = requestContext.getUriInfo().getQueryParameters();
        List<String> status = pathParameters.get("state");
        if(status!= null && !status.isEmpty()){
            return status.get(0);
        }
        return null;
    }

    private void checkUserExistent(WeChatUserInfo userInfo) {
        List<UserEntity> entity = userRepository.findByUnionid(userInfo.getUnionid());
        if (entity == null || entity.size() <= 0) {
            throw new BadRequestException(ErrorCode.USER_NOT_EXISTED);
        }
    }

    @Transactional
    private int registerUserIfNotExist(WeChatUserInfo userInfo) {
        List<UserEntity> byUnionid = userRepository.findByUnionid(userInfo.getUnionid());
        if (!byUnionid.isEmpty()) {
            return byUnionid.get(0).getUserId();
        }
        UserEntity entity = userEntityConverter.convert(userInfo);
        UserEntity saved = userRepository.save(entity);
        return saved.getUserId();
    }
}
