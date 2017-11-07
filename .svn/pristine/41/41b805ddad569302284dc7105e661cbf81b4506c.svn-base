/**
 * Baifubao.com,Inc. Copyright (c) 2000-2016 All Rights Reserved.
 */
package com.baidu.agentseller.base.util.common;

import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

/**
 * RSA工具类
 * 
 * @author zengzhong
 * @version RSAUtil.java 创建时间：2017年4月9日 下午10:08:52
 * 
 */
public class RSAUtil {

    /** 数字签名密钥算法 */
    public static final String KEY_ALGORITHM = "RSA";
    /** 数字名签名/验证算法 */
    public static final String SIGNATURE_ALGORITHM = "SHA256withRSA";
    /** RSA密钥长度 */
    public static final int KEY_SIZE = 2048;
    public static final String PUBLIC_KEY = "PUBLIC_KEY";
    public static final String PRIVATE_KEY = "PRIVATE_KEY";

    /**
     * 使用SHA256withRSA进行签名(签名结果表示成16进制字符串)
     * 
     * @param sourceStr 需要签名的原始字符串
     * @param privateKeyStr RSA私钥(16进制字符串表示)
     * @return
     */
    public static String sign(String sourceStr, String privateKeyStr, String charset) {
        try {
            byte[] sourceData = null;
            if (charset != null && !"".equals(charset)) {
                sourceData = sourceStr.getBytes(charset);
            } else {
                sourceData = sourceStr.getBytes();
            }
            byte[] privateKey = HexUtil.decodeHex(privateKeyStr.toCharArray());
            // 转换私钥材料
            PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(privateKey);
            // 实例化密钥工厂
            KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
            // 取私钥匙对象
            PrivateKey priKey = keyFactory.generatePrivate(pkcs8KeySpec);
            // 实例化Signature
            Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
            // 初始化Signature
            signature.initSign(priKey);
            // 更新
            signature.update(sourceData);
            // 签名
            byte[] result = signature.sign();
            return HexUtil.encodeHexString(result);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * SHA256withRSA进行验签
     * 
     * @param sourceStr 需要签名的原始字符串
     * @param sign 签名(16进制字符串表示)
     * @param publicKey RSA公钥(16进制字符串表示)
     * @return
     */
    public static boolean verify(String sourceStr, String sign, String publicKey, String charset) {
        try {
            byte[] sourceData = null;
            if (charset != null && !"".equals(charset)) {
                sourceData = sourceStr.getBytes(charset);
            } else {
                sourceData = sourceStr.getBytes();
            }
            byte[] publicKeyBytes = HexUtil.decodeHex(publicKey.toCharArray());
            byte[] signBytes = HexUtil.decodeHex(sign.toCharArray());
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicKeyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
            PublicKey pubKey = keyFactory.generatePublic(keySpec);
            // 实例化Signature
            Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
            // 初始化Signature
            signature.initVerify(pubKey);
            // 加载数据
            signature.update(sourceData);
            // 验证
            return signature.verify(signBytes);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static Map<String, String> generateKeyPair() {
        Map<String, String> keyMap = new HashMap<String, String>();
        try {
            KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance(KEY_ALGORITHM);
            keyPairGen.initialize(KEY_SIZE);
            KeyPair keyPair = keyPairGen.generateKeyPair();
            keyMap.put(PUBLIC_KEY, HexUtil.encodeHexString(keyPair.getPublic().getEncoded()));
            keyMap.put(PRIVATE_KEY, HexUtil.encodeHexString(keyPair.getPrivate().getEncoded()));
            return keyMap;
        } catch (Throwable e) {

        }
        return keyMap;
    }

}
