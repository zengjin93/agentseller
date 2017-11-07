package com.baidu.agentseller.biz.weixin.impl;


import com.baidu.agentseller.biz.weixin.TemplateMessageService;
import me.chanjar.weixin.common.exception.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpConfigStorage;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.template.WxMpTemplateData;
import me.chanjar.weixin.mp.bean.template.WxMpTemplateMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Created by v_zengjin on 2017/10/30 0030.
 */
@Service
public class TemplateMessageServiceImpl implements TemplateMessageService {

    protected Logger logger = LoggerFactory.getLogger(getClass());

    // 模板消息字体颜色
    private static final String TEMPLATE_FRONT_COLOR = "#32CD32";
    @Autowired
    protected WxMpConfigStorage configStorage;
    @Autowired
    protected WxMpService wxMpService;



    @Override
    public String sendPaySuccessMessage(String openId,Map<String, String> params) {
        String templateId="";
        String url="";
        return sendTemplateMessage(openId,templateId,url,params);
    }



    @Override
    public String sendOrderBusConfirmMessage(String openId, Map<String, String> params) {
        String templateId="";
        String url="";
        return sendTemplateMessage(openId,templateId,url,params);
    }


    @Override
    public String sendPayFreightMessage(String openId,Map<String, String> params) {
        String templateId="";
        String url="";
        return sendTemplateMessage(openId,templateId,url,params);
    }

    @Override
    public String sendPayFreightSuccessMessage(String openId,Map<String, String> params) {
        //发送邮费支付成功消息
        String templateId="";
        String url="";
        return sendTemplateMessage(openId,templateId,url,params);
    }

    @Override
    public String sendDeliverMessage(String openId,Map<String, String> params) {
        String templateId="";
        String url="";
        return sendTemplateMessage(openId,templateId,url,params);
    }

    @Override
    public String sendCusRefundMessage(String openId,Map<String, String> params) {
        String templateId="";
        String url="";
        return sendTemplateMessage(openId,templateId,url,params);
    }

    @Override
    public String sendRefundSuccessMessage(String openId,Map<String, String> params) {
        String templateId="";
        String url="";
        return sendTemplateMessage(openId,templateId,url,params);
    }



    /**
     * 发送模板消息
     * @param openId
     * @param templateId
     * @param url
     * @param params
     * @return
     */
    private String sendTemplateMessage(String openId, String templateId, String url, Map<String, String> params) {
        WxMpTemplateMessage template = new WxMpTemplateMessage();
        template.setToUser(openId);
        template.setTemplateId(templateId);
        template.setUrl(url);

        for (String key : params.keySet()) {
            WxMpTemplateData templateData = new WxMpTemplateData(key, params.get(key), TEMPLATE_FRONT_COLOR);
            template.addWxMpTemplateData(templateData);
        }
        try {
            return wxMpService.getTemplateMsgService()
                    .sendTemplateMsg(template);
        } catch (WxErrorException e) {
            logger.error(e.getMessage().toString());
        }
        return null;
    }


}
