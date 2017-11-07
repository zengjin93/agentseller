package com.baidu.agentseller.service.impl.wechat.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.github.binarywang.wxpay.config.WxPayConfig;
import com.github.binarywang.wxpay.service.WxPayService;
import com.github.binarywang.wxpay.service.impl.WxPayServiceImpl;

/**
 * 微信支付相关配置
 * <p>
 * Created by bjliumingbo on 2017/5/12.
 */
@Configuration
public class WxPayConfiguration {
    @Value("${wechat.pay.appId}")
    private String appId;

    @Value("${wechat.pay.mchId}")
    private String mchId;

    @Value("${wechat.pay.mchKey}")
    private String mchKey;

    @Value("${wechat.pay.subAppId}")
    private String subAppId;

    @Value("${wechat.pay.subMchId}")
    private String subMchId;

    @Value("${wechat.pay.keyPath}")
    private String keyPath;

    @Bean
    public WxPayConfig payConfig() {
        WxPayConfig payConfig = new WxPayConfig();
        payConfig.setAppId(this.appId);
        payConfig.setMchId(this.mchId);
        payConfig.setMchKey(this.mchKey);
        payConfig.setSubAppId(this.subAppId);
        payConfig.setSubMchId(this.subMchId);
        payConfig.setKeyPath(this.keyPath);

        return payConfig;
    }

    @Bean
    public WxPayService payService() {
        WxPayService payService = new WxPayServiceImpl();
        payService.setConfig(payConfig());
        return payService;
    }
}