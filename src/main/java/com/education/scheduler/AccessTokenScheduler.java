package com.education.scheduler;

import com.education.auth.WeChatAccessState;
import com.education.service.WeChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Hashtable;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by yzzhao on 1/10/16.
 */
@Component
public class AccessTokenScheduler {

    private static final Logger logger = Logger.getLogger(AccessTokenScheduler.class.getName());

    private Map<WeChatAccessState, String> accessTokens = new Hashtable<>();

    private String jsApiTicket;

    @Autowired
    private WeChatService weChatService;

    @PostConstruct
    public void postConstruct(){
        getAccessTokenScheduler();
    }

    @Scheduled(cron = "0 0 0/1 * * ?")
    public void getAccessTokenScheduler() {
        logger.info("access token scheduler");
        String accessToken = weChatService.requestAccessToken(WeChatAccessState.WECHAT.name());
        accessTokens.put(WeChatAccessState.WECHAT, accessToken);
        accessToken = weChatService.requestAccessToken(WeChatAccessState.WEB.name());
        accessTokens.put(WeChatAccessState.WEB, accessToken);
        jsApiTicket = weChatService.getJSApiTicket(accessTokens.get(WeChatAccessState.WECHAT));
        logger.info("get access token "+accessTokens);
    }

    public String getAccessToken(String state) {
        try {
            WeChatAccessState accessState = WeChatAccessState.valueOf(state);
            return getAccessToken(accessState);
        } catch (IllegalArgumentException e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
        }

        return null;
    }

    public String getJsApiTicket() {
        return jsApiTicket;
    }

    public String getAccessToken(WeChatAccessState state){
        return accessTokens.get(state);
    }

}
