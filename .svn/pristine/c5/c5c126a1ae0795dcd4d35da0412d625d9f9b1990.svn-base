/**
 * Baifubao.com,Inc. Copyright (c) 2000-2016 All Rights Reserved.
 */
package com.baidu.agentseller.base.util.common;

import java.security.MessageDigest;

/**
 * MD5签名工具类
 * 
 * @author zengzhong
 * @version MD5Util.java 创建时间：2017年4月10日 下午11:14:05
 * 
 */
public class MD5Util {

    /**
     * 使用MD5计算签名（消息摘要）
     * 
     * @param sourceStr 需要签名的原始字符串
     * @param keyStr 签名密钥(16进制字符串表示)
     * @return
     */
    public static String sign(String sourceStr, String keyStr, String charset) {
        String data = sourceStr + "&key=" + keyStr;
        try {
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            byte[] dataByte = null;
            if (charset != null && !"".equals(charset)) {
                dataByte = data.getBytes(charset);
            } else {
                dataByte = data.getBytes();
            }
            mdInst.update(dataByte);
            byte[] result = mdInst.digest();
            return HexUtil.encodeHexString(result);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 使用MD5计算签名（消息摘要）
     * 
     * @param sourceStr 需要签名的原始字符串
     * @param sign 签名(16进制字符串表示)
     * @param keyStr 签名密钥(16进制字符串表示)
     * @return
     */
    public static boolean verify(String sourceStr, String sign, String keyStr, String charset) {
        String data = sourceStr + "&key=" + keyStr;
        try {
            byte[] dataByte = null;
            if (charset != null && !"".equals(charset)) {
                dataByte = data.getBytes(charset);
            } else {
                dataByte = data.getBytes();
            }
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            mdInst.update(dataByte);
            byte[] result = mdInst.digest();
            String signResult = HexUtil.encodeHexString(result);
            return equals(signResult, sign);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean equals(String str1, String str2) {
        if (str1 == null) {
            return str2 == null;
        }

        return str1.equals(str2);
    }

}
