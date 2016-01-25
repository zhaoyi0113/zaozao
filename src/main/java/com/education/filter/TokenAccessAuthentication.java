package com.education.filter;

import com.education.auth.TokenAccess;
import com.education.db.entity.UserEntity;
import com.education.exception.BadRequestException;
import com.education.exception.ErrorCode;
import com.education.service.LoginHistoryService;
import com.education.service.WeChatUserInfo;
import com.education.service.converter.WeChatUserConverter;
import com.education.ws.util.ContextKeys;
import com.education.ws.util.HeaderKeys;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.logging.Logger;

/**
 * Created by yzzhao on 1/11/16.
 */
@Provider
public class TokenAccessAuthentication implements ContainerRequestFilter {

    private static final Logger logger = Logger.getLogger(TokenAccessAuthentication.class.getName());

    @Context
    private ResourceInfo resourceInfo;

    @Autowired
    private LoginHistoryService historyService;

    @Autowired
    private WeChatUserConverter converter;

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        Method resourceMethod = resourceInfo.getResourceMethod();
        Class<?> resourceClass = resourceInfo.getResourceClass();
        TokenAccess annotation = resourceMethod.getAnnotation(TokenAccess.class);
        if (annotation == null) {
            annotation = resourceClass.getAnnotation(TokenAccess.class);
        }
        if (annotation == null) {
            return;
        }
        MultivaluedMap<String, String> pathParameters = requestContext.getHeaders();
        List<String> tokens = pathParameters.get(HeaderKeys.ACCESS_TOKEN);
        if (annotation.requireAccessToken() && (tokens == null || tokens.isEmpty())) {
            throw new BadRequestException(ErrorCode.NOT_LOGIN);
        }
        if (tokens == null || tokens.isEmpty()) {
            return;
        }
        String token = tokens.get(0);
        logger.info("get token " + token);
        requestContext.setProperty(HeaderKeys.ACCESS_TOKEN, token);
        if (token != null) {
            UserEntity userEntity = historyService.getUserByToken(token);
            WeChatUserInfo user = converter.convert(userEntity);
            requestContext.setProperty(ContextKeys.WECHAT_USER, user);
        }
    }

}
