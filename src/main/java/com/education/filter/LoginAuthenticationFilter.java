package com.education.filter;

import com.education.auth.Login;
import com.education.auth.Public;
import com.education.exception.BadRequestException;
import com.education.exception.ErrorCode;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.Context;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.util.logging.Logger;

/**
 * Created by yzzhao on 12/28/15.
 */
@Provider
public class LoginAuthenticationFilter implements ContainerRequestFilter {
    private static final Logger logger = Logger.getLogger(LoginAuthenticationFilter.class.getName());

    @Context
    private HttpServletRequest servletRequest;

    @Context
    private ResourceInfo resourceInfo;

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        Login annotation = resourceInfo.getResourceClass().getAnnotation(Login.class);
        if (annotation == null) {
            return;
        }
        String userName = (String) servletRequest.getSession().getAttribute("user_name");
        if(userName == null){
            throw new BadRequestException(ErrorCode.NOT_LOGIN);
        }
        logger.info("logged in name "+userName);
    }
}
