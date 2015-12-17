package com.education.filter;

import com.education.service.WeChatService;
import com.education.service.WeChatUserInfo;
import com.education.ws.util.ContextKeys;
import com.education.ws.util.HeaderKeys;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.util.logging.Logger;

/**
 * Created by yzzhao on 12/13/15.
 */
@Provider
public class WeChatAuthenticationFilter implements ContainerRequestFilter {

    private static final Logger logger = Logger.getLogger(WeChatAuthenticationFilter.class.getName());

    @Autowired
    private WeChatService weChatService;

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        logger.info("wechat authentication filter");
        String openid = requestContext.getHeaderString(HeaderKeys.WECHAT_OPENID);
        if (openid != null) {
            logger.info("get openid " + openid);
            WeChatUserInfo userInfo = weChatService.getUserInfo(openid);
            if (userInfo!=null && "1".equals(userInfo.getSubscribe())) {
                requestContext.setProperty(ContextKeys.WECHAT_USER, userInfo);
            }
        } else {
            logger.severe("can't get user openid");
        }
        if (requestContext.getProperty(ContextKeys.WECHAT_USER) == null) {
            requestContext.setProperty(ContextKeys.WECHAT_USER, null);
        }
        logger.info("whether wechat user "+requestContext.getProperty(ContextKeys.WECHAT_USER));

    }
}
