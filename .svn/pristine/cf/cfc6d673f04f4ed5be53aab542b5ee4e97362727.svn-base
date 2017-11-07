/**
 * Copyright 2013-2013 baidu.com
 */
package com.baidu.agentseller.dal.pagination.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.druid.filter.config.ConfigTools;
import com.alibaba.druid.util.DruidPasswordCallback;

/**
 * 数据库连接加密密码解密回调实现
 * 
 * @author tianhuang
 */
public class EncryptedPasswordCallback extends DruidPasswordCallback {

    private static final long   serialVersionUID    = -7500855514327430557L;

    private static final Logger logger              = LoggerFactory
                                                              .getLogger(EncryptedPasswordCallback.class);

    /**
     * 私钥，带有默认值
     */
    public static final String  DEFAULT_PRIVATE_KEY = "MIIBVAIBADANBgkqhkiG9w0BAQEFAASCAT4wggE6AgEAAkEAocbCrurZGbC5GArEHKlAfDSZi7gFBnd4yxOt0rwTqKBFzGyhtQLu5PRKjEiOXVa95aeIIBJ6OhC2f8FjqFUpawIDAQABAkAPejKaBYHrwUqUEEOe8lpnB6lBAsQIUFnQI/vXU4MV+MhIzW0BLVZCiarIQqUXeOhThVWXKFt8GxCykrrUsQ6BAiEA4vMVxEHBovz1di3aozzFvSMdsjTcYRRo82hS5Ru2/OECIQC2fAPoXixVTVY7bNMeuxCP4954ZkXp7fEPDINCjcQDywIgcc8XLkkPcs3Jxk7uYofaXaPbg39wuJpEmzPIxi3k0OECIGubmdpOnin3HuCP/bbjbJLNNoUdGiEmFL5hDI4UdwAdAiEAtcAwbm08bKN7pwwvyqaCBC//VnEWaq39DCzxr+Z2EIk=";

    /**
     * 私钥，带有默认值
     */
    public static final String  DEFAULT_PUBLIC_KEY  = "MFwwDQYJKoZIhvcNAQEBBQADSwAwSAJBAKHGwq7q2RmwuRgKxBypQHw0mYu4BQZ3eMsTrdK8E6igRcxsobUC7uT0SoxIjl1WveWniCASejoQtn/BY6hVKWsCAwEAAQ==";

    /**
     * 公钥存储位置，如果此位置被设定，则从此文件中根据KEY值取出公钥
     */
    private String              publicKeyPath       = null;

    /**
     * 公钥，带有默认值
     */
    private String              publicKey           = DEFAULT_PUBLIC_KEY;

    /**
     * 秘钥属性文件中存放公钥的KEY
     */
    private String              key                 = "publicKey";

    /**
     * pass 解密后的密钥
     */
    private String realPass = null;

    public EncryptedPasswordCallback() {
        super("prompt", false);
    }

    public EncryptedPasswordCallback(String prompt, boolean echoOn) {
        super(prompt, echoOn);
    }
    
    public static void main(String[] args) {
        String s = "Daigou!2017";
        try {
            System.out.println(ConfigTools.encrypt(DEFAULT_PRIVATE_KEY, s));
            String ss = "IkoYifewrqLgzh3ncFNwnEqcw5yyjyaYeofaG9S1os0EDK3Yt44d37q1Wi2mjNiOQM8SEU5q0jcnbmpHEx2EFg==";
            System.out.println(ConfigTools.decrypt(DEFAULT_PUBLIC_KEY, ss));
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * 初始化私钥
     */
    public void initPublicKey() {
        if (StringUtils.isNotBlank(publicKeyPath)) {
            InputStream in = null;

            try {
                // 从文件中取出私钥
                in = new FileInputStream(publicKeyPath);
                Properties props = new Properties();
                props.load(in);
                String pk = props.getProperty(key);

                if (StringUtils.isNotBlank(pk)) {
                    publicKey = pk;
                }
            } catch (IOException e) {
                logger.warn("get publicKey error", e);
            } finally {
                try {
                    if (in != null) {
                        in.close();
                    }
                } catch (IOException e) {
                    logger.error("close file Exception", e);
                }
            }
        }
    }

    /**
     * Druid将会通过反射来调用这个方法，设定数据库访问密码
     */
    public void setProperties(Properties properties) {
        // 获取配置之后的经过加密的密码
        String pas = properties.getProperty("password");
        String newPass = pas;
        if(StringUtils.isNotBlank(realPass)){
            super.setPassword(realPass.toCharArray());
            return;
        }
        try {
            initPublicKey();
            newPass = ConfigTools.decrypt(publicKey, pas);
            realPass=newPass;
        } catch (Exception e) {
            logger.error("set real password error", e);
        }
        super.setPassword(newPass.toCharArray());
    }



    public void setKey(String key) {
        this.key = key;
    }

    public void setPublicKeyPath(String publicKeyPath) {
        this.publicKeyPath = publicKeyPath;
    }
}
