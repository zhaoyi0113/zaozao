package com.education.filter;

import com.education.auth.TokenAccess;
import com.education.exception.BadRequestException;
import com.education.exception.ErrorCode;
import com.education.ws.util.HeaderKeys;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.List;

/**
 * Created by yzzhao on 1/11/16.
 */
@Provider
public class TokenAccessAuthentication implements ContainerRequestFilter {


    @Context
    private ResourceInfo resourceInfo;

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
        if(tokens == null || tokens.isEmpty()){
            return;
        }
        String token = tokens.get(0);
        requestContext.setProperty(HeaderKeys.ACCESS_TOKEN, token);
    }

}
