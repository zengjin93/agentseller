/**
 * Baifubao.com,Inc. Copyright (c) 2000-2016 All Rights Reserved.
 */
package com.baidu.agentseller.base.util.common;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * AES加解密工具类
 * 
 * @author zengzhong
 * @version AESUtil.java 创建时间：2017年4月9日 下午10:08:36
 * 
 */
public class AESUtil {
    private static final byte[] AES_IV = initIv();

    /**
     * 使用标准的AES加密算法(加密结果表示成16进制字符串)
     * 
     * @param originalText 待加密的字符串
     * @param encryptionKey 密钥(16进制字符串表示)
     * @return
     */
    public static String encrypt(String originalText, String encryptionKey, String charset) {
        try {
            // 获得加密的密钥流
            byte[] md = HexUtil.decodeHex(encryptionKey.toCharArray());
            SecretKeySpec key = new SecretKeySpec(md, "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            byte[] byteContent = null;
            if (charset != null || !"".equals(charset)) {
                byteContent = originalText.getBytes(charset);
            } else {
                byteContent = originalText.getBytes();
            }
            IvParameterSpec iv = new IvParameterSpec(AES_IV);
            cipher.init(Cipher.ENCRYPT_MODE, key, iv);
            byte[] result = cipher.doFinal(byteContent);
            return HexUtil.encodeHexString(result);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 使用标准的AES解密算法(密文需要是16进制字符串)
     * 
     * @param ciphertext 密文
     * @param decryptionKey 密钥(16进制字符串表示)
     * @return
     */
    public static String decrypt(String ciphertext, String decryptionKey, String charset) {
        try {
            // 获得解密的密钥流
            byte[] md = HexUtil.decodeHex(decryptionKey.toCharArray());
            SecretKeySpec key = new SecretKeySpec(md, "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            byte[] decoderBtye = HexUtil.decodeHex(ciphertext.toCharArray());
            IvParameterSpec iv = new IvParameterSpec(AES_IV);
            cipher.init(Cipher.DECRYPT_MODE, key, iv);
            byte[] result = cipher.doFinal(decoderBtye);
            return new String(result, charset);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 初始向量的方法, 全部为0。针对AES算法,IV值一定是128位的(16字节).
     */
    private static byte[] initIv() {
        int blockSize = 16;
        byte[] iv = new byte[blockSize];
        for (int i = 0; i < blockSize; ++i) {
            iv[i] = 0;
        }
        return iv;
    }
}
