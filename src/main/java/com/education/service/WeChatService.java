package com.education.service;

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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.MalformedURLException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by yzzhao on 12/12/15.
 */
@Service("WeChatService")
public class WeChatService {


    @Value("#{config['zaozao']}")
    private String token;

    @Value("#{config['wechat_appid']}")
    private String appid;

    @Value("#{config['wechat_appsecret']}")
    private String appSecret;

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

    private static final Logger logger = Logger.getLogger(WeChatService.class.getName());

    public boolean validateConnection(String signature, String timeStamp, String nonce) {
        String[] tmpArr = {token, timeStamp, nonce};
        Arrays.sort(tmpArr);
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < tmpArr.length; i++) {
            builder.append(tmpArr[i]);
        }
        String tmpStr = builder.toString();
        String sha1 = getSha1String(tmpStr);

        logger.info("get tmp str " + tmpStr);
        logger.info("get sha1 " + sha1);
        if (sha1 != null && sha1.equals(signature)) {
            return true;
        }
        return false;
    }

    public WeChatUserInfo getWebUserInfo(String code) {
        Map tokenMap = getWebSiteAccessToken(code);
        String token = (String) tokenMap.get("access_token");
        String openid = (String) tokenMap.get("openid");
        String url = buildWebUserInfoUrl(token, openid);
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

    public Map getWebSiteAccessToken(String code) {
        String url = buildWebAccessTokenURL(code);
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
        String accessToken = requestAccessToken();
        HttpGet httpGet = new HttpGet(buildGetUserInfoURL(accessToken, openid));
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

    public String getQRBarCodeURL(String code) {
        String ticket = getQRBarTicket(code);
        logger.info("get qr bar ticket "+ticket);
        String url = buildQRCodeUrl(ticket);
        HttpGet httpGet = new HttpGet(url);
        HttpClient httpClient = HttpClients.createDefault();
        try {
            HttpResponse response = httpClient.execute(httpGet);
            HttpEntity entity = response.getEntity();
            String body = EntityUtils.toString(entity, "UTF-8").trim();
            logger.info("get user list response " + body);
            Gson gson = new Gson();
            Map<String, Object> userList = gson.fromJson(body, Map.class);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public String getQRBarTicket(String code){
        String url = buildQRCodeTicketUrl(requestAccessToken());
        HttpPost httpPost = new HttpPost(url);
        HttpClient httpClient = HttpClients.createDefault();
        try {
            Map<String, String> params =new Hashtable<>();
            params.put("expire_seconds", "30");
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
        }catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<String> getUserOpenIDList() {
        String accessToken = requestAccessToken();

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

    private String requestAccessToken() {
        try {
            HttpGet httpGet = new HttpGet(buildAccessTokenURL());
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
        }
        return null;
    }

    private String buildAccessTokenURL() {
        StringBuilder builder = new StringBuilder();
        builder.append(accessTokenUrl).append("&appid=").append(appid).append("&secret=").append(appSecret);
        return builder.toString();
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

    private String buildWebAccessTokenURL(String code) {
        StringBuilder builder = new StringBuilder();
        builder.append(webAccessTokenUrl).append("?appid=").append(appid).append("&secret=").append(appSecret).append("&code=").append(code).append("&grant_type=authorization_code");
        return builder.toString();
    }

    private String buildWebUserInfoUrl(String token, String openid) {
        StringBuilder builder = new StringBuilder();
        builder.append(webUserInfoUrl).append("?access_token=").append(token).append("&openid=").append(openid);
        return builder.toString();
    }

    private String buildQRCodeTicketUrl(String token){
        StringBuilder builder = new StringBuilder();
        builder.append(qrCodeTicketUrl).append("?access_token=").append(token);
        return builder.toString();
    }

    private String buildQRCodeUrl(String token){
        StringBuilder builder = new StringBuilder();
        builder.append(qrCodeUrl).append("?ticket=").append(token);
        return builder.toString();
    }

    private static String getSha1String(String decript) {
        try {
            MessageDigest digest = java.security.MessageDigest
                    .getInstance("SHA-1");
            digest.update(decript.getBytes());
            byte messageDigest[] = digest.digest();
            // Create Hex String
            StringBuffer hexString = new StringBuffer();
            // 字节数组转换为 十六进制 数
            for (int i = 0; i < messageDigest.length; i++) {
                String shaHex = Integer.toHexString(messageDigest[i] & 0xFF);
                if (shaHex.length() < 2) {
                    hexString.append(0);
                }
                hexString.append(shaHex);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

}