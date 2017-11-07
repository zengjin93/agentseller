package com.baidu.agentseller.service.impl.wechat.handler;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import com.baidu.agentseller.dal.dao.UserDao;
import com.baidu.agentseller.dal.entity.User;
import com.baidu.agentseller.service.impl.wechat.service.CoreService;

import me.chanjar.weixin.common.exception.WxErrorException;
import me.chanjar.weixin.common.session.WxSessionManager;
import me.chanjar.weixin.mp.api.WxMpConfigStorage;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutTextMessage;
import me.chanjar.weixin.mp.bean.result.WxMpUser;

/**
 * 用户关注公众号Handler
 * <p>
 * Created by FirenzesEagle on 2016/7/27 0027. Email:liumingbo2008@gmail.com
 */
@Component
public class SubscribeHandler extends AbstractHandler {

    private static final Logger logger = LoggerFactory.getLogger(SubscribeHandler.class);

    @Autowired
    protected WxMpConfigStorage configStorage;
    @Autowired
    protected WxMpService wxMpService;
    @Autowired
    protected CoreService coreService;

    @Autowired
    private UserDao userDao;

    @Autowired
    private TransactionTemplate transactionTemplate;

    @Override
    public WxMpXmlOutMessage handle(WxMpXmlMessage wxMessage, Map<String, Object> context, WxMpService wxMpService,
            WxSessionManager sessionManager) throws WxErrorException {
        final WxMpUser wxMpUser = coreService.getUserInfo(wxMessage.getFromUser(), "zh_CN");
        /*
         * List<NameValuePair> params = new ArrayList<>(); params.add(new BasicNameValuePair("openId",
         * wxMpUser.getOpenId())); params.add(new BasicNameValuePair("nickname", wxMpUser.getNickname()));
         * params.add(new BasicNameValuePair("headImgUrl", wxMpUser.getHeadImgUrl()));
         */
        // TODO(user) 在这里可以进行用户关注时对业务系统的相关操作（比如新增用户）
        new Thread(new Runnable() {
            @Override
            public void run() {
                transactionTemplate.execute(new TransactionCallbackWithoutResult() {
                    @Override
                    protected void doInTransactionWithoutResult(TransactionStatus status) {
                        try {
                            User user = userDao.getUserByOpenId(wxMpUser.getOpenId(), true);
                            if (user != null) {
                                User update = new User();
                                update.setId(user.getId());
                                update.setFocus(true);
                                if (userDao.updateById(user) < 1) {
                                    logger.warn("SubscribeHandler update user error,openid=" + user.getOpenId());
                                }
                            }

                        } catch (Exception e) {

                        }
                    }
                });

            }
        });

        WxMpXmlOutTextMessage m = WxMpXmlOutMessage.TEXT().content("尊敬的" + wxMpUser.getNickname() + "，您好！,感谢您的关注！")
                .fromUser(wxMessage.getToUser()).toUser(wxMessage.getFromUser()).build();
        logger.info("subscribeMessageHandler" + m.getContent());

        return m;
    }
};
