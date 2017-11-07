package com.baidu.agentseller.web.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.baidu.agentseller.service.api.exception.AgentsellerErrorCode;
import com.baidu.agentseller.service.api.model.GenericResponse;
import com.google.gson.Gson;

/**
 * 拦截不在白名单中的用户信息请求
 * 
 * @author v_zengjin
 *
 */
public class UserInfoInterceptor extends HandlerInterceptorAdapter {

    private static final Logger logger = LoggerFactory.getLogger(UserInfoInterceptor.class);

    private static final String[] whiteList = { "127.0.0.1", "172.18.18.18", "172.18.23.23" };

    /**
     * 拦截不在白名单中的用户信息请求
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {

        String ip = getClientIp(request);
        boolean hit = false;
        for (String white : whiteList) {
            if (StringUtils.equals(ip, white)) {
                hit = true;
                break;
            }
        }
        if (!hit) {
            logger.warn("UserInfoController#setUserInfo ip not in whiteList,ip=", ip);
            response.reset();
            response.setContentType("application/json");
            response.setCharacterEncoding("utf-8");
            response.getWriter()
                    .print(new Gson().toJson(new GenericResponse<String>(AgentsellerErrorCode.PERMISSION_DENIED)));
            return false;
        }

        return true;
    }

    /**
     * 获取真实用户ip
     * 
     * @param request
     * @return
     */
    private String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        logger.info("gatewayServiceImpl#getClientIp, ip = " + ip);

        return ip;
    }

}
