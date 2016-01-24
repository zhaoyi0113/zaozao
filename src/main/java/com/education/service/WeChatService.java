package com.education.service;

import com.education.auth.WeChatAccessState;
import com.education.scheduler.AccessTokenScheduler;
import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by yzzhao on 12/12/15.
 */
@Service("WeChatService")
public class WeChatService {

    @Value("#{config['wechat_token']}")
    private String token;

    @Value("#{config['wechat_service_appid']}")
    private String srvAppId;

    @Value("#{config['wechat_service_appsecret']}")
    private String srvAppSecret;

    @Value("#{config['wechat_access_token_url']}")
    private String accessTokenUrl;

    @Value("#{config['wechat_get_user_list_url']}")
    private String userListUrl;

    @Value("#{config['wechat_get_user_info_url']}")
    private String userInfoUrl;

    @Value("#{config['wechat_web_site_access_token_url']}")
    private String webAccessTokenUrl;

    @Value("#{config['wechat_web_site_user_info']}")
    private String webUserInfoUrl;

    @Value("#{config['wechat_qrcode_ticket_url']}")
    private String qrCodeTicketUrl;

    @Value("#{config['wechat_qrcode_url']}")
    private String qrCodeUrl;

    @Value("#{config['wechat_jsapi_ticket_url']}")
    private String jsapiTicketUrl;

    @Value("#{config['wechat_qr_connect_url']}")
    private String qrWebConnectUrl;

    @Value("#{config['wechat_web_appid']}")
    private String webAppId;

    @Value("#{config['wechat_web_secret']}")
    private String webAppSecret;

    @Value("#{config['wechat_sub_appid']}")
    private String subAppId;

    @Value("#{config['wechat_sub_appsecret']}")
    private String subAppSecret;

    @Value("#{config['wechat_redirect_uri']}")
    private String redirectUri;

    @Autowired
    private AccessTokenScheduler tokenScheduler;

    @Autowired
    private SignatureGenerator signatureGenerator;

    private static final Logger logger = Logger.getLogger(WeChatService.class.getName());

    private Map<String, String> jsapiTicketCache;

    public boolean validateConnection(String signature, String timeStamp, String nonce) {
        logger.info("validate connection " + signature + ", " + timeStamp + ", " + nonce + ", " + token);
        String[] tmpArr = {token, timeStamp, nonce};
        Arrays.sort(tmpArr);
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < tmpArr.length; i++) {
            builder.append(tmpArr[i]);
        }
        String tmpStr = builder.toString();
        String sha1 = signatureGenerator.getSha1String(tmpStr);

        logger.info("get tmp str " + tmpStr);
        logger.info("get sha1 " + sha1);
        if (sha1 != null && sha1.equals(signature)) {
            return true;
        }
        return false;
    }

    public WeChatUserInfo getWebUserInfo(String code, String state) {
        Map tokenMap = getWebSiteAccessToken(code, state);
        if (tokenMap == null) {
            return null;
        }
        String token = (String) tokenMap.get("access_token");
        String openid = (String) tokenMap.get("openid");
        if (token == null || openid == null) {
            return null;
        }
        String url = buildWebUserInfoUrl(token, openid);
        logger.info("get user info url:" + url);
        HttpGet httpGet = new HttpGet(url);
        HttpClient httpClient = HttpClients.createDefault();
        HttpResponse response = null;
        try {
            response = httpClient.execute(httpGet);
            HttpEntity entity = response.getEntity();
            String body = EntityUtils.toString(entity, "UTF-8").trim();
            logger.info("get web user info response " + body);
            Gson gson = new Gson();
            WeChatUserInfo weChatUserInfo = gson.fromJson(body, WeChatUserInfo.class);
            return weChatUserInfo;
        } catch (IOException e) {
            e.printStackTrace();
            logger.log(Level.SEVERE, e.getMessage(), e);
        }
        return null;
    }

    public Map<String, String> getWebJSSignature(String url, String state) {
        String noncestr = "zaozao";
        String timestamp = System.currentTimeMillis() + "";
        String jsTicket = tokenScheduler.getJsApiTicket();
        StringBuilder builder = new StringBuilder("jsapi_ticket=");
        builder.append(jsTicket).append("&noncestr=").append(noncestr)
                .append("&timestamp=").append(timestamp).append("&url=").append(url);
        String signature = signatureGenerator.getSha1String(builder.toString());
        Map<String, String> jsSingautre = new Hashtable<>();
        jsSingautre.put("noncestr", noncestr);
        jsSingautre.put("timestamp", timestamp);
        jsSingautre.put("jsapi_ticket", jsTicket);
        jsSingautre.put("appid", getAppId(WeChatAccessState.valueOf(state)));
        jsSingautre.put("signature", signature);
        return jsSingautre;
    }

    private Map getJSApiTicket() {
        String accessToken = tokenScheduler.getAccessToken(WeChatAccessState.WECHAT_SERVICE);
        String url = buildJSApiTicketUrl(accessToken);
        logger.info("get jsapi ticket url:" + url);
        HttpGet httpGet = new HttpGet(url);
        HttpClient httpClient = HttpClients.createDefault();
        HttpResponse response = null;
        try {
            response = httpClient.execute(httpGet);
            HttpEntity entity = response.getEntity();
            String body = EntityUtils.toString(entity, "UTF-8").trim();
            logger.info("get js api ticket response " + body);
            Gson gson = new Gson();
            Map<String, String> map = gson.fromJson(body, Map.class);
            return map;
        } catch (IOException e) {
            e.printStackTrace();
            logger.log(Level.SEVERE, e.getMessage(), e);
        }
        return new Hashtable<>();
    }

    public String getJSApiTicket(String accessToken) {
        String url = buildJSApiTicketUrl(accessToken);
        logger.info("get jsapi ticket url:" + url);
        HttpGet httpGet = new HttpGet(url);
        HttpClient httpClient = HttpClients.createDefault();
        HttpResponse response = null;
        try {
            response = httpClient.execute(httpGet);
            HttpEntity entity = response.getEntity();
            String body = EntityUtils.toString(entity, "UTF-8").trim();
            logger.info("get js api ticket response " + body);
            Gson gson = new Gson();
            Map<String, String> map = gson.fromJson(body, Map.class);
            return map.get("ticket");
        } catch (IOException e) {
            e.printStackTrace();
            logger.log(Level.SEVERE, e.getMessage(), e);
        }
        return null;
    }

    public Map getWebSiteAccessToken(String code, String state) {
        String url = buildWebAccessTokenURL(code, state);
        HttpGet httpGet = new HttpGet(url);
        HttpClient httpClient = HttpClients.createDefault();
        try {
            HttpResponse response = httpClient.execute(httpGet);
            HttpEntity entity = response.getEntity();
            String body = EntityUtils.toString(entity, "UTF-8").trim();
            logger.info("get web access token response " + body);
            Gson gson = new Gson();
            Map map = gson.fromJson(body, Map.class);
            logger.info("get web access token " + map);
            return map;
        } catch (IOException e) {
            e.printStackTrace();
            logger.log(Level.SEVERE, e.getMessage(), e);
        }
        return null;
    }

    public WeChatUserInfo getUserInfo(String openid) {
        String accessToken = tokenScheduler.getAccessToken(WeChatAccessState.WECHAT_SERVICE);
        String url = buildGetUserInfoURL(accessToken, openid);
        logger.info("get user info url:" + url);
        HttpGet httpGet = new HttpGet(url);
        HttpClient httpClient = HttpClients.createDefault();
        try {
            HttpResponse response = httpClient.execute(httpGet);
            HttpEntity entity = response.getEntity();
            String body = EntityUtils.toString(entity, "UTF-8").trim();
            logger.info("get user info response " + body);
            Gson gson = new Gson();
            WeChatUserInfo userInfo = gson.fromJson(body, WeChatUserInfo.class);
            return userInfo;
        } catch (IOException e) {
            e.printStackTrace();
            logger.log(Level.SEVERE, e.getMessage(), e);
        }
        return null;
    }

    public String getQRBarCodeURL(String code, String state) {
        String ticket = getQRBarTicket(code, state);
        logger.info("get qr bar ticket " + ticket);
        String url = buildQRCodeUrl(ticket);
        logger.info("show qr bar url:" + url);
        return url;
    }

    public String getQrWebConnectUrl() {
        StringBuilder builder = new StringBuilder(qrWebConnectUrl);
        builder.append("?appid=").append(webAppId).append("&redirect_uri=").append("http://imzao.com&response_type=code&scope=snsapi_login&state=123#wechat_redirect");
        logger.info("generate qr connect url " + builder.toString());
        return builder.toString();

    }

    public String getQRBarTicket(String code, String state) {
        String url = buildQRCodeTicketUrl(tokenScheduler.getAccessToken(state));
        HttpPost httpPost = new HttpPost(url);
        HttpClient httpClient = HttpClients.createDefault();
        try {
            Map<String, String> params = new Hashtable<>();
            params.put("expire_seconds", "300");
            params.put("action_name", "QR_SCENE");
            Gson gson = new Gson();

            StringEntity strEntity = new StringEntity(gson.toJson(params));

            httpPost.setEntity(strEntity);
            HttpResponse response = httpClient.execute(httpPost);
            HttpEntity entity = response.getEntity();
            String body = EntityUtils.toString(entity, "UTF-8").trim();
            logger.info("get qr barcode ticket response " + body);
            Map map = gson.fromJson(body, Map.class);
            return (String) map.get("ticket");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<String> getUserOpenIDList() {
        String accessToken = tokenScheduler.getAccessToken(WeChatAccessState.WECHAT_SERVICE);
        String uri = buildGetUserListURL(accessToken);
        logger.info("get user list url " + uri);
        HttpGet httpGet = new HttpGet(uri);
        HttpClient httpClient = HttpClients.createDefault();
        try {
            HttpResponse response = httpClient.execute(httpGet);
            HttpEntity entity = response.getEntity();
            String body = EntityUtils.toString(entity, "UTF-8").trim();
            logger.info("get user list response " + body);
            Gson gson = new Gson();
            Map<String, Object> userList = gson.fromJson(body, Map.class);
            if (userList != null && userList.containsKey("data")) {
                LinkedTreeMap data = (LinkedTreeMap) userList.get("data");
                ArrayList openidList = (ArrayList) data.get("openid");
                logger.info("open id list " + openidList);
                return openidList;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String requestAccessToken(String state) {
        try {
            WeChatAccessState accessState = WeChatAccessState.valueOf(state);
            HttpGet httpGet = new HttpGet(buildAccessTokenURL(accessState));
            HttpClient httpClient = HttpClients.createDefault();
            HttpResponse response = httpClient.execute(httpGet);
            HttpEntity entity = response.getEntity();
            String body = EntityUtils.toString(entity, "UTF-8").trim();
            logger.info("get access token response " + body);
            Gson gson = new Gson();
            Map<String, String> accessToken = gson.fromJson(body, Map.class);
            return accessToken.get("access_token");
        } catch (MalformedURLException e) {
            e.printStackTrace();
            logger.log(Level.SEVERE, e.getMessage(), e);
        } catch (IOException e) {
            e.printStackTrace();
            logger.log(Level.SEVERE, e.getMessage(), e);
        } catch (IllegalArgumentException e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
        }
        return null;
    }

    public URI getRedirectUri(String token, String redirect) {
        URI uri = null;
        try {
            if (token == null) {
                uri = new URI(redirectUri);
            } else {
                if (redirect != null) {
                    uri = new URI(redirect + "&token=" + token);
                } else {
                    uri = new URI(redirectUri + "?token=" + token);
                }
            }
        } catch (URISyntaxException e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
        }
        return uri;

    }

    private String buildAccessTokenURL(WeChatAccessState accessState) {
        StringBuilder builder = new StringBuilder();
        String id = getAppId(accessState);
        String sec = getAppSecret(accessState);
        builder.append(accessTokenUrl).append("&appid=").append(id).append("&secret=").append(sec);
        return builder.toString();
    }

    private String getAppId(WeChatAccessState state) {
        String id = srvAppId;
        switch (state) {
            case WECHAT_SERVICE:
                id = srvAppId;
                break;
            case WECHAT_SUBSCRIBER:
                id = subAppId;
                break;
            case WEB:
                id = webAppId;
                break;
            case MOBILE:
                break;
            default:
                id = srvAppId;
        }
        return id;
    }

    private String getAppSecret(WeChatAccessState state) {
        String sec = srvAppSecret;
        switch (state) {
            case WECHAT_SERVICE:
                sec = srvAppSecret;
                break;
            case WECHAT_SUBSCRIBER:
                sec = subAppSecret;
                break;
            case WEB:
                sec = webAppSecret;
                break;
            case MOBILE:
                break;
            default:
                sec = srvAppSecret;
        }
        return sec;
    }

    private String buildGetUserListURL(String accessToken) {
        StringBuilder builder = new StringBuilder();
        builder.append(userListUrl).append("?access_token=").append(accessToken);
        return builder.toString();
    }

    private String buildGetUserInfoURL(String accessToken, String openid) {
        StringBuilder builder = new StringBuilder();
        builder.append(userInfoUrl).append("?access_token=").append(accessToken).append("&openid=").append(openid).append("&lang=zh_CN");
        return builder.toString();
    }

    private String buildWebAccessTokenURL(String code, String state) {
        StringBuilder builder = new StringBuilder();
        String appId = null;
        String appSec = null;
        try {
            appId = getAppId(WeChatAccessState.valueOf(state));
            appSec = getAppSecret(WeChatAccessState.valueOf(state));
        } catch (IllegalArgumentException e) {
            appId = getAppId(WeChatAccessState.WECHAT_SERVICE);
            appSec = getAppSecret(WeChatAccessState.WECHAT_SERVICE);
        }
        builder.append(webAccessTokenUrl).append("?appid=").append(appId).append("&secret=").append(appSec).append("&code=").append(code).append("&grant_type=authorization_code");
        return builder.toString();
    }

    private String buildWebUserInfoUrl(String token, String openid) {
        StringBuilder builder = new StringBuilder();

        builder.append(webUserInfoUrl).append("?access_token=").append(token).append("&openid=").append(openid);
        return builder.toString();
    }

    private String buildQRCodeTicketUrl(String token) {
        StringBuilder builder = new StringBuilder();
        builder.append(qrCodeTicketUrl).append("?access_token=").append(token);
        return builder.toString();
    }

    private String buildQRCodeUrl(String token) {
        StringBuilder builder = new StringBuilder();
        builder.append(qrCodeUrl).append("?ticket=").append(token);
        return builder.toString();
    }

    private String buildJSApiTicketUrl(String token) {
        StringBuffer builder = new StringBuffer(jsapiTicketUrl);
        builder.append("?access_token=").append(token).append("&type=jsapi");
        return builder.toString();
    }

}
