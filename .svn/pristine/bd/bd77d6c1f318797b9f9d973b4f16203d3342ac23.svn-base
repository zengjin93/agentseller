package com.baidu.agentseller.biz.user.impl;

import com.baidu.agentseller.base.util.common.AESUtil;
import com.baidu.agentseller.base.util.common.DateUtil;
import com.baidu.agentseller.base.util.common.JsonUtil;
import com.baidu.agentseller.base.util.common.StringUtil;
import com.baidu.agentseller.base.util.common.constants.CommonConstants;
import com.baidu.agentseller.biz.user.UserService;
import com.baidu.agentseller.dal.dao.UserDao;
import com.baidu.agentseller.dal.entity.User;
import com.baidu.agentseller.service.integration.rest.redis.RedisClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;

@Service("userService")
public class UserServiceImpl implements UserService {

    private static final String AES_KEY = "56fed1c10467ac469da74ae0776876b7c4dc4eca662c5ba0c7a0305395218125";

    @Autowired
    RedisClient redisClient;

    @Autowired
    UserDao userDao;

    @Override
    public String getUserLoginInfoByJsessionId(String jsessionId) {

        String decryptKey = AESUtil.decrypt(jsessionId, AES_KEY, CommonConstants.UTF_8);
        String jsessionInfo = redisClient.get(decryptKey);
        if (StringUtil.isEmpty(jsessionInfo)) {
            // 从库中取信息
            User userInfo = userDao.getUserByJsessionId(decryptKey);
            Map<String, String> info = JsonUtil.readJson2Map(userInfo.getSessionInfo());

            Date oldTime = DateUtil.parse(info.get(CommonConstants.TIMESTAMP), DateUtil.LONG_FORMAT);
            if (DateUtil.addHours(oldTime, 1).after(new Date())) {
                return userInfo.getSessionInfo();
            }
            return null;

        }
        redisClient.setex(decryptKey, jsessionInfo, 60 * 60);
        return jsessionInfo;
    }

}
