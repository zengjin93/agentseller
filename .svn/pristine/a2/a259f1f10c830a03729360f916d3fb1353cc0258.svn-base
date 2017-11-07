package com.baidu.agentseller.biz.weixin;

import java.util.Map;

/**
 * Created by v_zengjin on 2017/10/30 0030.
 */
public interface TemplateMessageService {

    /**
     * 付款成功发送消息给买家
     *
     * @param params
     * @return
     */
    String sendPaySuccessMessage(String openId, Map<String, String> params);

    /**
     * 商户确认订单发送消息给买家
     *
     * @param openId
     * @param params
     * @return
     */
    String sendOrderBusConfirmMessage(String openId, Map<String, String> params);

    /**
     * 商户设定邮费后发送支付邮费消息给买家
     *
     * @param params
     * @return
     */
    String sendPayFreightMessage(String openId, Map<String, String> params);

    /**
     * 支付邮费成功发送消息给买家
     *
     * @param params
     * @return
     */
    String sendPayFreightSuccessMessage(String openId, Map<String, String> params);

    /**
     * 发货成功发送消息给买家
     *
     * @param params
     * @return
     */
    String sendDeliverMessage(String openId, Map<String, String> params);

    /**
     * 用户请求退款发送消息给商户
     *
     * @param params
     * @return
     */
    String sendCusRefundMessage(String openId, Map<String, String> params);

    /**
     * 商户确认退款发送消息给买家
     *
     * @param params
     * @return
     */
    String sendRefundSuccessMessage(String openId, Map<String, String> params);


}
