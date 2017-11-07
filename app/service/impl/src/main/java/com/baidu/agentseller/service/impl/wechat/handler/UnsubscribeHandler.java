package com.baidu.agentseller.service.impl.wechat.handler;

import me.chanjar.weixin.common.session.WxSessionManager;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import com.baidu.agentseller.dal.dao.UserDao;
import com.baidu.agentseller.dal.entity.User;

import java.util.Map;

/**
 * @author Binary Wang(https://github.com/binarywang)
 */
@Component
public class UnsubscribeHandler extends AbstractHandler {

    private static final Logger logger = LoggerFactory.getLogger(SubscribeHandler.class);

    @Autowired
    private UserDao userDao;

    @Autowired
    private TransactionTemplate transactionTemplate;

    @Override
    public WxMpXmlOutMessage handle(WxMpXmlMessage wxMessage, Map<String, Object> context, WxMpService wxMpService,
            WxSessionManager sessionManager) {
        final String openId = wxMessage.getFromUser();
        logger.info("取消关注用户 OPENID: " + openId);
        // TODO 可以更新本地数据库为取消关注状态
        transactionTemplate.execute(new TransactionCallbackWithoutResult() {
            @Override
            protected void doInTransactionWithoutResult(TransactionStatus status) {
                try {
                    User user = userDao.getUserByOpenId(openId, true);
                    if (user != null) {
                        User update = new User();
                        update.setId(user.getId());
                        update.setFocus(false);
                        if (userDao.updateById(user) < 1) {
                            logger.warn("UnsubscribeHandler update user error,openid=" + user.getOpenId());
                        }
                    }

                } catch (Exception e) {

                }
            }
        });

        return null;
    }

}
