package com.education.service;

import com.education.exception.BadRequestException;
import com.education.exception.ErrorCode;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by yzzhao on 1/18/16.
 */
@Service("WeChatPayService")
public class WeChatPayService {

    private static final Logger logger = Logger.getLogger(WeChatPayService.class.getName());

    public static final String DEFAULT_ENCODING = "UTF-8";

    @Value("#{config['wechat_pay_url']}")
    private String payUrl;

    @Value("#{config['wechat_service_appid']}")
    private String serviceAppId;

    @Value("#{config['wechat_mch_id']}")
    private String mchId;

    @Value("#{config['wechat_device_info']}")
    private String deviceInfo;

    @Value("#{config['wechat_pay_notify_url']}")
    private String notifyUrl;

    @Value("#{config['wechat_pay_secret']}")
    private String paySecret;

    @Autowired
    private SignatureGenerator signatureGenerator;

    public Map<String, String> requestPay(String openId, String proDesc, String proDetail, int price, String ip) {
        logger.info("request pay openId=" + openId + " description=" + proDesc + ", detail=" + proDetail + " price=" + price + " ip=" + ip);
        String xml = generatePayRequestXML(openId, proDesc, proDetail, price, ip);
        HttpPost httpPost = new HttpPost(payUrl);
        HttpClient httpClient = HttpClients.createDefault();
        try {
            StringEntity postParam = new StringEntity(xml);
            httpPost.setEntity(postParam);
            HttpResponse response = httpClient.execute(httpPost);
            HttpEntity entity = response.getEntity();
            String body = EntityUtils.toString(entity, "UTF-8").trim();
            logger.info("get pay request response " + body);
            Map<String, String> ret = parseXML(body);
            logger.info("parse response xml "+ret);
            Map<String, String> returnMap = new Hashtable<>();
            if(ret.containsKey("return_code")){
                returnMap.put("return_code", ret.get("return_code"));
                if(returnMap.get("return_code").equalsIgnoreCase("SUCCESS")) {
                    returnMap.put("return_msg", ret.get("return_msg"));
                    returnMap.put("result_code", ret.get("result_code"));
                    returnMap.put("prepay_id", ret.get("prepay_id"));
                    Map<String, String> paySign = generatePaysign(ret.get("prepay_id"));
                    returnMap.put("sign", paySign.get("sign"));
                    returnMap.put("timestamp", paySign.get("timestamp"));
                    returnMap.put("nonce_str", paySign.get("nonce_str"));
                }
            }
            return returnMap;
        } catch (IOException e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
            throw new BadRequestException(ErrorCode.PAY_FAILED);
        }

    }

    private Map<String, String> generatePaysign(String prepayId){
        String timeStamp = String.valueOf(System.currentTimeMillis());
        String nonceStr = String.valueOf(System.currentTimeMillis());
        StringBuffer buffer = new StringBuffer();
        buffer.append("appId=").append(serviceAppId).append("&nonceStr=").append(nonceStr)
                .append("&package=").append(prepayId).append("&signType=MD5&timeStamp=")
                .append(timeStamp).append("&key=").append(paySecret);
        String md5 = signatureGenerator.getMD5String(buffer.toString());
        Map<String, String> sign = new Hashtable<>();
        sign.put("timestamp", timeStamp);
        sign.put("sign", md5.toUpperCase());
        sign.put("nonce_str", nonceStr);
        return sign;
    }

    private String generatePayRequestXML(String openId, String proDesc,
                                         String proDetail, int price, String ip) {
        Map<String, String> payData = new Hashtable<>();
        payData.put("appid", serviceAppId);
        payData.put("mch_id", mchId);
        payData.put("device_info", deviceInfo);
        payData.put("nonce_str", String.valueOf(System.currentTimeMillis()));
        payData.put("body", proDesc);
        payData.put("detail", proDetail);
        payData.put("out_trade_no", generateOutTradeNo());
        payData.put("fee_type", "CNY");
        payData.put("total_fee", String.valueOf(price));
        payData.put("spbill_create_ip", ip);
        Calendar instance = Calendar.getInstance();
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
        String startDate = format.format(instance.getTime());
        payData.put("time_start", startDate);
        instance.add(Calendar.MINUTE, 10);
        String endDate = format.format(instance.getTime());
        payData.put("time_expire", endDate);
        payData.put("notify_url", notifyUrl);
        payData.put("trade_type", "JSAPI");
        payData.put("openid", openId);
        payData.put("sign", generateSign(payData).toUpperCase());
        String request = generateXMLRequest(payData);
        logger.info("generated pay map " + payData);
        logger.info("generated pay request body:" + request);
        return request;
    }

    private String generateSign(Map<String, String> payData) {
        Map<String, String> treeMap = new TreeMap<String, String>(new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return o1.compareTo(o2);
            }
        });
        treeMap.putAll(payData);
        StringBuffer buffer = new StringBuffer();
        for (Map.Entry<String, String> entry : treeMap.entrySet()) {
            buffer.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
        }
        buffer.append("key").append("=").append(paySecret);
        String signStr = buffer.toString();
        return signatureGenerator.getMD5String(signStr);
    }

    private static String generateXMLRequest(Map<String, String> request) {
        String xml = "<xml>";

        Iterator<Map.Entry<String, String>> iter = request.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry<String, String> entry = iter.next();
            String key = entry.getKey();
            String val = entry.getValue();
            xml += "<" + key + ">" + val + "</" + key + ">";
        }
        xml += "</xml>";
        return xml;
    }

    /**
     * 生成商户订单号
     *
     * @return
     */
    private static String generateOutTradeNo() {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        String salt = String.valueOf(System.currentTimeMillis());
        DataOutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream);
        try {
            dataOutputStream.write(salt.getBytes(DEFAULT_ENCODING));
            dataOutputStream.flush();
            return DigestUtils.md2Hex(byteArrayOutputStream.toByteArray());
        } catch (IOException e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
        }
        return null;
    }

    private static Map<String, String> parseXML(String xml) {
        Map<String, String> map = new HashMap<String, String>();
        try {
            Document doc = DocumentHelper.parseText(xml);
            Element root = doc.getRootElement();
            List<Element> elements = root.elements();
            for (Element element : elements) {
                map.put(element.getName(), element.getStringValue());
            }
        } catch (DocumentException e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
        }
        return map;
    }
}
