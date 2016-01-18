package com.education.service;

import org.springframework.stereotype.Component;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Logger;

/**
 * Created by yzzhao on 1/18/16.
 */
@Component
public class SignatureGenerator {

    private static final Logger logger = Logger.getLogger(SignatureGenerator.class.getName());

    public String getSha1String(String decript){
        return signString(decript, "SHA-1");
    }

    public String getMD5String(String descript){
        return signString(descript, "MD5");
    }

    private static String signString(String decript, String algorithm) {
        try {
            logger.info(algorithm+" on string:"+decript);
            MessageDigest digest = java.security.MessageDigest
                    .getInstance(algorithm);
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
