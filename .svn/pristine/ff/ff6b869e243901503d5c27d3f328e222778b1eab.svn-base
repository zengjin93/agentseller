package com.baidu.agentseller.web.user;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import com.baidu.agentseller.biz.user.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.baidu.agentseller.base.util.common.AESUtil;
import com.baidu.agentseller.base.util.common.MapUtil;
import com.baidu.agentseller.base.util.common.RSAUtil;
import com.baidu.agentseller.base.util.common.StringUtil;
import com.baidu.agentseller.base.util.common.constants.CommonConstants;
import com.baidu.agentseller.dal.dao.UserDao;
import com.baidu.agentseller.dal.entity.User;
import com.baidu.agentseller.service.api.exception.AgentsellerErrorCode;
import com.baidu.agentseller.service.api.exception.AgentsellerException;
import com.baidu.agentseller.service.api.model.GenericResponse;
import com.baidu.agentseller.service.integration.rest.redis.RedisClient;
import com.google.gson.Gson;

/**
 * 用户信息同步相关接口(需配置白名单)
 *
 * @author v_zengjin
 */
@Controller
@RequestMapping("/userinfo")
public class UserInfoController {

    private static final Logger logger = LoggerFactory.getLogger(UserInfoController.class);

    private static final String PUBLIC_KEY =
            "30820122300d06092a864886f70d01010105000382010f003082010a0282010100d67d91bae89de56ea474fcf049136ac46b8f63b0639f5af54a519e85be5eb2679b3b68bc05a550a707b23eb299624ce75d2f3bf55649000d21b3ea513c9bb093625b64a69f62ad573bc54347cbfa70986f96b330f8ddf06b5cdd03ed44613dc8e58359f436f9d9dc3067782896b6374ea11213f26ae99ede556ac1927021bcaceb0ae9c556dbccd9d1dba8359de4493edbdb815fd0e68005cb91f617082c94e9697fc50e285b9d3612c89fac047d6db8a6ee8cdc4024764f590d389c0a4575e38516cfdaca417ff6acb5f223f0ea3e8cd111f122364e1da6a0fc4b24e02918cba6b5a4307a395f793d1b1021605320342c4c3de50239e73797cabd6986dd84350203010001";

    private static final String AES_KEY = "56fed1c10467ac469da74ae0776876b7c4dc4eca662c5ba0c7a0305395218125";

    @Autowired
    RedisClient redisClient;

    @Autowired
    UserDao userDao;

    @Autowired
    UserService userService;

    @Autowired
    private TransactionTemplate transactionTemplate;
    
    /**
     * 设置用户信息到redis
     *
     * @param userId
     * @param openId
     * @param name
     * @param timestamp
     * @param sign
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/setUserInfo", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public GenericResponse<String> setUserInfo(
            @RequestParam("userId") final String userId, @RequestParam("openId") String openId,
            @RequestParam("name") String name, @RequestParam("timestamp") String timestamp,
            @RequestParam("sign") String sign) throws Exception {
        try {
            Map<String, String> jsonMap = new HashMap<String, String>();
            jsonMap.put("userId", userId);
            jsonMap.put("openId", openId);
            jsonMap.put("name", name);
            jsonMap.put("timestamp", timestamp);
            logger.info("UserInfoController#setUserInfo requestPatams=", jsonMap);
            if (!RSAUtil.verify(MapUtil.mapToStr(jsonMap, CommonConstants.SIGN), sign, PUBLIC_KEY,
                    CommonConstants.UTF_8)) {
                logger.warn("UserInfoController#setUserInfo checkSign error,requestPatams=", jsonMap);
                return new GenericResponse<String>(AgentsellerErrorCode.CHECK_SIGN_ERROR);
            }
            final String json = new Gson().toJson(jsonMap);
            final String key = userId + CommonConstants.UNDERLINE + openId + CommonConstants.UNDERLINE + name
                    + CommonConstants.UNDERLINE + timestamp;
            try {
                redisClient.setex(key, json, 60 * 60);
            } catch (Exception e) {
                // redis异常继续往数据库存入
                logger.warn("UserInfoController#setUserInfo redis server error", e);
            }
            // 存入数据库
            transactionTemplate.execute(new TransactionCallbackWithoutResult() {
                @Override
                protected void doInTransactionWithoutResult(TransactionStatus status) {
                    User userInfo = userDao.getUserById(Integer.parseInt(userId), true);
                    if (userInfo != null) {
                        User updateUser = new User();
                        updateUser.setId(userInfo.getId());
                        updateUser.setJsessionId(key);
                        updateUser.setSessionInfo(json);
                        userDao.updateById(updateUser);
                        return;
                    }
                    throw new AgentsellerException(AgentsellerErrorCode.SYSTEM_ERROR);
                }
            });

            String encryptKey = AESUtil.encrypt(key, AES_KEY, CommonConstants.UTF_8);
            return new GenericResponse<String>(encryptKey);
        } catch (Exception e) {
            logger.error("UserInfoController#setUserInfo error", e);
            return new GenericResponse<String>(AgentsellerErrorCode.SYSTEM_ERROR);
        }

    }

    /**
     * 通过jsessionid获取用户登录信息，
     *
     * @param response
     * @param jsessionId
     * @param timestamp
     * @param sign
     * @return
     */
    @RequestMapping(value = "/getUserInfo", method = {RequestMethod.GET, RequestMethod.POST})
    public GenericResponse<String> getUserInfo(HttpServletResponse response,
                                               @RequestParam(value = "jsessionId") String jsessionId, @RequestParam(value = "timestamp") String timestamp,
                                               @RequestParam(value = "sign") String sign) {

        try {
            Map<String, String> jsonMap = new HashMap<String, String>();
            jsonMap.put("jsessionId", jsessionId);
            jsonMap.put("timestamp", timestamp);

            if (!RSAUtil.verify(MapUtil.mapToStr(jsonMap, CommonConstants.SIGN), sign, PUBLIC_KEY,
                    CommonConstants.UTF_8)) {
                logger.warn("UserInfoController#getUserInfo checkSign error,requestPatams=", jsonMap);
                return new GenericResponse<String>(AgentsellerErrorCode.CHECK_SIGN_ERROR);
            }
            String jsessionInfo = userService.getUserLoginInfoByJsessionId(jsessionId);
            if (StringUtil.isEmpty(jsessionInfo)) {
                return new GenericResponse<String>(AgentsellerErrorCode.NO_LOGIN);
            }
            return new GenericResponse<String>(jsessionInfo);
        } catch (Exception e) {
            logger.error("UserInfoController#getUserInfo error", e);
            return new GenericResponse<String>(AgentsellerErrorCode.SYSTEM_ERROR);
        }
    }

}
