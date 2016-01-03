package com.education.filter;

import com.education.auth.Login;
import com.education.auth.Public;
import com.education.exception.BadRequestException;
import com.education.exception.ErrorCode;
import com.education.service.BackendLoginService;
import com.education.service.BackendUserService;
import com.education.ws.util.ContextKeys;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.logging.Logger;

/**
 * Created by yzzhao on 1/3/16.
 */
@Provider
public class AdminPasswordAuthentication implements ContainerRequestFilter {

    private static final Logger logger = Logger.getLogger(AdminPasswordAuthentication.class.getName());

    @Context
    private HttpServletRequest servletRequest;

    @Context
    private ResourceInfo resourceInfo;

    @Context
    private BackendLoginService loginService;

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        Public annotation = resourceInfo.getResourceMethod().getAnnotation(Public.class);
        if (annotation == null || !annotation.requireAdminPassword()) {
            return;
        }
        String userName = (String) servletRequest.getSession().getAttribute("user_name");
        if(ContextKeys.ADMIN_ACCOUNT.equals(userName)){
            return;
        }
        if (annotation.requireAdminAccount()) {
            throw new BadRequestException(ErrorCode.INVALID_USER);
        }
        String pwd = getAdminPassword(requestContext);
        if(!loginService.checkUserAccount(ContextKeys.ADMIN_ACCOUNT, pwd)){
            throw new BadRequestException(ErrorCode.INVALID_PASSWORD);
        }
    }

    private String getAdminPassword(ContainerRequestContext context){
        MultivaluedMap<String, String> pathParameters = context.getHeaders();
        List<String> password = pathParameters.get("password");
        logger.info("check admin password "+password);
        if(password != null && !password.isEmpty()){
            return password.get(0);
        }
        Collection<String> propertyNames = context.getPropertyNames();
        for(String name: propertyNames){
            logger.info("property "+name+"="+context.getProperty(name));
        }
        throw new BadRequestException(ErrorCode.INVALID_PASSWORD);
    }
}
