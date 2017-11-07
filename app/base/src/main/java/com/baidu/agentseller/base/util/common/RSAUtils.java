package com.baidu.agentseller.base.util.common;

/**
 * Copyright 2013-2014 Baifubao.com
 */

import java.io.ByteArrayOutputStream;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.Cipher;

import org.apache.commons.codec.binary.Base64;

/**
 * <p>
 * RSA公钥/私钥/签名工具包
 * </p>
 * <p>
 * 字符串格式的密钥在未在特殊说明情况下都为BASE64编码格式<br/>
 * 由于非对称加密速度极其缓慢，一般文件不使用它来加密而是使用对称加密，<br/>
 * 非对称加密算法可以用来对对称加密的密钥加密，这样保证密钥的安全也就保证了数据的安全
 * </p>
 * 
 * @author tianhuang
 * @date 2014-4-1
 * @version 1.0
 */
public class RSAUtils {

    /**
     * 移动端私钥
     */
    public final static String MOBILE_PRIVATE_KEY =
            "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQDGeWfGr7a6SDLIxa3ha4paaB2gA4s/FcCZ4UKZkFg1/3lSOzWipc4N5DBJveB7juKTo6ZqnPnf333Xeh4LGOFTMCRhZMPC+TFO+gkj68EMhfzU7E9FzJuMH+jQOEvZnnpap/SEz6bmkSKK07BHxooOt/qZnUCJgVcCmQN4DDfJFDs7lfeZbH4RYstYXoSJJ4MmOh5/5BxlhAV4ykrEqHiimNkgTo52loHVNMH+vvlsn38YquMY0LnAQ7yWVa82ramMbXq6N7+lDdWKk8SOFp704UubUk1aO/4zk3n8kjlEkxxFV3Bxqj+rxVAUsfuSv3X+wbR/QzSgBg9/+44w0OV5AgMBAAECggEAJ2ix33TCbqipVM5ToV/uH9duknhBrOwPyz48MCvGKCqrF1XkZ4BOstltcSb/rOkJOlGTWLYEs5w365I25vMwwojboG8Wo99BGDJzoCtOsMLmGg02DGD9PcyUOMwE25gpKVkSJ1DoDf4hgrlX1liozxVe8COWTfddpIlnc+3RId7F5d9f2tIjLic8iCL/Z+bswHpnmBV0UCZkXvi5v9Sla1YwlCc//IGz8UsCg/xHqNjl32z0cgviQyz8bYq3hci/Rbrtmyktqj0fQdVmhiJNeWDBVnu0c5DCingZ7CnVOcjhU6wbnzHDR1E5NZ3n8KAcKJxGtAtmu2lM7Vc6goG7AQKBgQDtSlhqPX2F06veROAi4Wk/Rrk2lwkLq7FLqNTrQCIOu+A3Jp5Ln/MKoZY2hbwUfyvXUb9bFBJbYzI5eRH1w8Sm/U9CWCXCNxwiS5KZIlhRQZRYLNqRFcbqZ+UizPCw2gIgRKdj2+gUytc1lfGfHUZDc6xHOVryn1jXaT7gUhLy2QKBgQDWH5BrLxR8QicB3N89JRIyBaVMgX6nuJv3dY+SncHcBcW3eIV4aWwUDTgiAf7ycK1NPLkh7+xqiixBTIj4h3Z5jKevnf9S9TeldNr1VI2A4A0jcp4oSNcVlNIyMkCUFvSfxDmiP5q79nY1RdEIWRCuVDpTmA7hLXh4WMFdPLajoQKBgQC6FS1U0zfWdPI48gZbFC33Az1HxBXB4zi8PoAKpFaQ+2CPn2dPovVzhthHlYSzBi+ZQXAwuoLSy+2dCIa/FnDnC4rWWk3yCkmz01P5ZuefgTAWHcahNFSziho0ukaeSbiVGYGzW8aaV5+BVD8RKmHpj5KwoBfkYHXJJ625OGPHsQKBgQDKi1cIHKPLGqRdXboJ08xPZwnCwee17aoTPbBO502evBMYBzhx+38RxKNpf+vTF4THP/57vMEs0oEkT6ptNW57cfKY6+K+bVly+IO6d33W/5lOA7jZsHVMY9clRmXR75j8nBhMgs/ypyOQJvehZuahkOkPdBH3LRT8+jAG60x0oQKBgAMxwnCqBH23C2KgkAbhMlk2GdnvTpMu6s9HAkRU3jp85eBbCa0C9ffKRyOS9MUrb8M6qrww3AGuoJ1ybCFy7+8xMi5lNpkYs7QhLkU/XskKfjQ6ji4fz+kzva5Xsjo8MuhRxt0Tjr/LmI2Yn+x37ieB5Fl8OH1S6HRTHMK0AvbQ";

    /** */
    /**
     * 获取公钥的key
     */
    private static final String PUBLIC_KEY = "RSAPublicKey";

    /** */
    /**
     * 获取私钥的key
     */
    private static final String PRIVATE_KEY = "RSAPrivateKey";

    /**
     * 加密算法RSA
     */
    public static final String KEY_ALGORITHM = "RSA";

    /**
     * RSA最大加密明文大小
     */
    private static final int MAX_ENCRYPT_BLOCK = 117;

    /**
     * RSA最大解密密文大小
     */
    private static final int MAX_DECRYPT_BLOCK = 128;

    /**
     * RSA最大解密密文大小
     */
    public static final Base64 base64Util = new Base64();

    /** */
    /**
     * <p>
     * 生成密钥对(公钥和私钥)
     * </p>
     * 
     * @return
     * @throws Exception
     */
    public static Map<String, Object> genKeyPair() throws Exception {
        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance(KEY_ALGORITHM);
        keyPairGen.initialize(1024);
        KeyPair keyPair = keyPairGen.generateKeyPair();
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
        Map<String, Object> keyMap = new HashMap<String, Object>(2);
        keyMap.put(PUBLIC_KEY, publicKey);
        keyMap.put(PRIVATE_KEY, privateKey);
        return keyMap;
    }

    /** */
    /**
     * <p>
     * 获取私钥
     * </p>
     * 
     * @param keyMap 密钥对
     * @return
     * @throws Exception
     */
    public static String getPrivateKey(Map<String, Object> keyMap) throws Exception {
        Key key = (Key) keyMap.get(PRIVATE_KEY);
        return base64Util.encodeToString(key.getEncoded());
    }

    /** */
    /**
     * <p>
     * 获取公钥
     * </p>
     * 
     * @param keyMap 密钥对
     * @return
     * @throws Exception
     */
    public static String getPublicKey(Map<String, Object> keyMap) throws Exception {
        Key key = (Key) keyMap.get(PUBLIC_KEY);
        return base64Util.encodeToString(key.getEncoded());
    }

    public static void main(String[] args) {
        try {
            // Map<String, Object> keyMap = genKeyPair();
            // String publicKey = getPublicKey(keyMap);
            // String privateKey = getPrivateKey(keyMap);
            //
            //
            // System.out.println(privateKey+"\n"+publicKey);
            //
            // String encrytpString =
            // encryptByPrivateKey("111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111".getBytes(),
            // privateKey);
            // System.out.println(encrytpString);
            //
            // byte[] aesEncryKey = Base64.decodeBase64(encrytpString);
            //
            //
            // byte[] strArray = decryptByPublicKey(aesEncryKey, publicKey);
            // System.out.println(new String(strArray));

            // System.out.println(new
            // String(decryptByPublicKey(Base64.decodeBase64("PvvCOElQwpZLlWOgTALY6RXg+1td9e1uPcTRebQfY3x9TzMG3uWt1BiijghoALONo0UFk4yodjFwuv2Pi7ex7cjpDOgana6t+PRiCaKC73DyjpJSPYXI+uk3Mk81yznFSPhMIGbGXu7MhxmemEnnSSKllJy+Cf0Ou7RGuLKvcXOahpe4k3meo14HBtVcpNZ+MzGfpcL0P2WvehvLld/kZoP2FFYOGR97tc/m5uL3VeA/2vXYQaP6GDDhC7TzKHihCPaudPkKCRkLkPoKSPLWl4txvTFLHXupwLFYQ8wjuthNgQZWJCthBF+vvCQCrd+ccj5p1Vu4MKMNZ2H5CWtoCxSAi5FquH8Vy7w6NbjkCmtlmlbhYm5Igj8+WwX0UjutPnJa7MsC6dyjbCDTjzk7voXwpz7Unpb0dZOzAYdE/shTEpF7sa8O1Ahhsv0+bwqWnS2TBOxNSjkc7EVdhDHLDu6Ac5YZLOUMT6VgDlgZ78k5mmTCm9PFrK7VmrhUyXgz"),
            // "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCryhA2yd+q60CpnRTTaIv1yh/UkUj6sJL+QmB5mENOVQm+336YdALj29fgRuuLhK73rIkMgfFOJdviWULKRyNDise0LEMR1BIOzkCW985nB9S4mG5O/ZhTzUQVACpX4K8nZZg0SmiXijinvOTe5gHR3Vqr2pHKbEg0h3XsBGCiPwIDAQAB")));

            // System.out.println(new
            // String(decryptByPrivateKey(Base64.decodeBase64("aaFKyVAaztEpUgF8jFUu+jCOoWDrFPIlcZ6oHpKKgytYQAkPvAvFHaPlr5LtLTIuTsI
            // IJ+WxF1E2tqRVK9FQioJq8djMZ254WDIukdv2Tw0h63muj57dPIPafLKU7l59vEgJOECRxQs0c7n6Dc/dLfW8eRMb/FZPZHT0mixifCp2A9hkCsC1jWrYwLe2Bk8ra
            // P9TPQTTRMvAL7NYxnuFHjgDYCglZA84LRbCl0cbtFTlYKasO37UTDmr4LMQiRlCzt728osqYHXvRskpUVDKFIxGwaGya0IuWg4dDifZMDEVKUXgO/QwckqPx4JU2nK3HxD
            // UQESrsZJTl8ONg+Vdqw=="),"MIICeAIBADANBgkqhkiG9w0BAQEFAASCAmIwggJeAgEAAoGBAKvKEDbJ36rrQKmdFNNoi/XKH9SRSPqwkv5CYHmYQ05VCb7ffph0AuPb1+BG64uErvesiQyB8U4l2+JZQspHI0OKx7QsQxHUEg7OQJb3zmcH1LiYbk79mFPNRBUAKlfgrydlmDRKaJeKOKe85N7mAdHdWqvakcpsSDSHdewEYKI/AgMBAAECgYEAg4AmzznS7CwvEGc9BOOXl6uEmBwqqDO4Ice/v/SNOQ4haqn8Z0kO2Ut1QaooLmF/6fl540go5jBW1DqNxPeVhqAGWM++aZnx39cX0Z3xYUD1ymf3MCD4QdMtHACM/2tJaBQDG0MgVCZe+xNFA0Y03PvkmRocwdBzxe+hYzg5s7ECQQDsKb+aNXZm9IzHXoIKFKnxEPLXi9PzAWgR+3ByQOV9J2pQclgkyffDMh8eZWXJURktXCMYqVdYgx+4EO173Cc5AkEAujgTXxqmBfOT+WHoPPbZwr0+cdjZ0qdwrTsFp7ZjBbfnTrQERoFSUOwKvPZO1otcmcxtpZzipF/aGon7M/jdNwJBAIJSI3ZwjC8SQmF+aJoxfvoVVTzW35YYr5pEu2+BGmKvDn7oKnXasguYLA30/G9UfmVGk+N3B0lDyowjYznXahkCQClMIk+nE0ainCBkLCLJA8YCPgT1eGkVJRHl0v08n303KZ70oTYZm+3CSydY4GcIBQM+qNpfjouxjibwjrsZ58UCQQDoduypOnYR/XbOcjH3Ky2Y8SvP19Pr8IomLpDfGJ2FjujLW+7F3RVYALD4+O+Y1BOUQ+jojIx5E+I4A9R1WgNo")));
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /** */
    /**
     * <p>
     * 公钥加密
     * </p>
     * 
     * @param data 源数据
     * @param publicKey 公钥(BASE64编码)
     * @return
     * @throws Exception
     */
    public static String encryptByPublicKey(byte[] data, String publicKey) throws Exception {
        byte[] keyBytes = base64Util.decode(publicKey);
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        Key publicK = keyFactory.generatePublic(x509KeySpec);
        // 对数据加密
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, publicK);
        int inputLen = data.length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offSet = 0;
        byte[] cache;
        int i = 0;
        // 对数据分段加密
        while (inputLen - offSet > 0) {
            if (inputLen - offSet > MAX_ENCRYPT_BLOCK) {
                cache = cipher.doFinal(data, offSet, MAX_ENCRYPT_BLOCK);
            } else {
                cache = cipher.doFinal(data, offSet, inputLen - offSet);
            }
            out.write(cache, 0, cache.length);
            i++;
            offSet = i * MAX_ENCRYPT_BLOCK;
        }
        byte[] encryptedData = out.toByteArray();
        out.close();
        // return encryptedData;
        return Base64.encodeBase64String(encryptedData);
    }

    /**
     * <p>
     * 公钥解密
     * </p>
     * 
     * @param encryptedData 已加密数据
     * @param publicKey 公钥(BASE64编码)
     * @return
     * @throws Exception
     */
    public static byte[] decryptByPublicKey(byte[] encryptedData, String publicKey) throws Exception {

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            byte[] keyBytes = Base64.decodeBase64(publicKey);
            X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
            Key publicK = keyFactory.generatePublic(x509KeySpec);
            Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
            cipher.init(Cipher.DECRYPT_MODE, publicK);
            int inputLen = encryptedData.length;
            int offSet = 0;
            byte[] cache;
            int i = 0;
            // 对数据分段解密
            while (inputLen - offSet > 0) {
                if (inputLen - offSet > MAX_DECRYPT_BLOCK) {
                    cache = cipher.doFinal(encryptedData, offSet, MAX_DECRYPT_BLOCK);
                } else {
                    cache = cipher.doFinal(encryptedData, offSet, inputLen - offSet);
                }
                out.write(cache, 0, cache.length);
                i++;
                offSet = i * MAX_DECRYPT_BLOCK;
            }
            byte[] decryptedData = out.toByteArray();
            return decryptedData;
        } catch (Exception e) {
            throw e;
        } finally {
            out.close();
        }
    }

    /** */
    /**
     * <p>
     * 公钥解密
     * </p>
     * 
     * @param encryptedData 已加密数据
     * @param publicKey 公钥(BASE64编码)
     * @return
     * @throws Exception
     */
    public static byte[] decryptByPublicKey2(byte[] encryptedData, String publicKey) throws Exception {
        byte[] keyBytes = Base64.decodeBase64(publicKey);
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        Key publicK = keyFactory.generatePublic(x509KeySpec);
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.DECRYPT_MODE, publicK);
        int inputLen = encryptedData.length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offSet = 0;
        byte[] cache;
        int i = 0;
        // 对数据分段解密
        while (inputLen - offSet > 0) {
            if (inputLen - offSet > MAX_DECRYPT_BLOCK) {
                cache = cipher.doFinal(encryptedData, offSet, MAX_DECRYPT_BLOCK);
            } else {
                cache = cipher.doFinal(encryptedData, offSet, inputLen - offSet);
            }
            out.write(cache, 0, cache.length);
            i++;
            offSet = i * MAX_DECRYPT_BLOCK;
        }
        byte[] decryptedData = out.toByteArray();
        out.close();
        return decryptedData;
    }

    /**
     * <p>
     * 私钥加密
     * </p>
     * 
     * @param data 源数据
     * @param privateKey 私钥(BASE64编码)
     * @return
     * @throws Exception
     */
    public static String encryptByPrivateKey(byte[] data, String privateKey) throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            byte[] keyBytes = Base64.decodeBase64(privateKey);
            PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
            RSAPrivateKey privateK = (RSAPrivateKey) keyFactory.generatePrivate(pkcs8KeySpec);
            Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
            cipher.init(Cipher.ENCRYPT_MODE, privateK);
            int inputLen = data.length;
            int offSet = 0;
            byte[] cache;
            int i = 0;
            // 对数据分段加密
            while (inputLen - offSet > 0) {
                if (inputLen - offSet > MAX_ENCRYPT_BLOCK) {
                    cache = cipher.doFinal(data, offSet, MAX_ENCRYPT_BLOCK);
                } else {
                    cache = cipher.doFinal(data, offSet, inputLen - offSet);
                }
                out.write(cache, 0, cache.length);
                i++;
                offSet = i * MAX_ENCRYPT_BLOCK;
            }
            byte[] encryptedData = out.toByteArray();
            return Base64.encodeBase64String(encryptedData);
        } catch (Exception e) {
            throw e;
        } finally {
            out.close();
        }
    }

    /**
     * <p>
     * 公钥解密
     * </p>
     * 
     * @param encryptedData 已加密数据
     * @param privateKey 私钥(BASE64编码)
     * @return
     * @throws Exception
     */
    public static byte[] decryptByPrivateKey(byte[] encryptedData, String privateKey) throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            byte[] keyBytes = Base64.decodeBase64(privateKey);
            PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(keyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
            Key privateK = keyFactory.generatePrivate(pkcs8EncodedKeySpec);
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.DECRYPT_MODE, privateK);
            int inputLen = encryptedData.length;
            out = new ByteArrayOutputStream();
            int offSet = 0;
            byte[] cache;
            int i = 0;
            // 对数据分段解密
            while (inputLen - offSet > 0) {
                if (inputLen - offSet > MAX_DECRYPT_BLOCK) {
                    cache = cipher.doFinal(encryptedData, offSet, MAX_DECRYPT_BLOCK);
                } else {
                    cache = cipher.doFinal(encryptedData, offSet, inputLen - offSet);
                }
                out.write(cache, 0, cache.length);
                i++;
                offSet = i * MAX_DECRYPT_BLOCK;
            }
            byte[] decryptedData = out.toByteArray();
            out.flush();
            return decryptedData;
        } catch (Exception e) {
            throw e;
        } finally {
            out.close();
        }

    }
}