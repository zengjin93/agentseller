package com.baidu.agentseller.web;

import com.baidu.agentseller.base.util.common.HttpUtil;
import com.baidu.agentseller.base.util.common.JsonUtil;
import com.baidu.agentseller.base.util.common.StringUtil;
import com.baidu.agentseller.base.util.common.constants.CommonConstants;
import com.baidu.agentseller.service.api.exception.AgentsellerErrorCode;
import com.baidu.agentseller.service.api.exception.AgentsellerException;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

public class BaseController {

    /**
     * 获取用户信息
     *
     * @param request
     * @return
     */
    protected Map<String, String> getUserInfo(HttpServletRequest request) {
        Map<String, String> resultMap = new HashMap<String, String>();
        String userInfo = (String) request.getSession().getAttribute("user_info");
        if (StringUtil.isEmpty(userInfo)) {
            throw new AgentsellerException(AgentsellerErrorCode.NO_LOGIN);
        }
        Map<String, String> userInfoMap = JsonUtil.readJson2Map(userInfo);
        resultMap.put(CommonConstants.USER_ID, userInfoMap.get(CommonConstants.USER_ID));
        resultMap.put(CommonConstants.OPEN_ID, userInfoMap.get(CommonConstants.OPEN_ID));
        return resultMap;
    }

    /**
     * 获取请求参数和用户信息
     * @param request
     * @return
     */
    protected Map<String, String> getRequestInfoAndUserInfo(HttpServletRequest request) {
        Map<String, String> params = HttpUtil.copyRequestParameter2Map(request);
        params.putAll(getUserInfo(request));// 获取登陆用户信息
        return params;
    }


}
