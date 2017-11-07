package com.baidu.agentseller.web.interceptor;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.baidu.agentseller.biz.user.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.baidu.agentseller.base.util.common.StringUtil;
import com.baidu.agentseller.service.api.exception.AgentsellerErrorCode;
import com.baidu.agentseller.service.api.model.GenericResponse;
import com.google.gson.Gson;

/**
 * 拦截未登录用户的请求
 * 
 * @author v_zengjin
 *
 */
public class LoginInfoInterceptor extends HandlerInterceptorAdapter {

    private static final Logger logger = LoggerFactory.getLogger(LoginInfoInterceptor.class);

    @Autowired
    UserService userService;

    /**
     * 拦截不在白名单中的用户信息请求
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {

        String userInfo = (String) request.getSession().getAttribute("user_info");
        if (StringUtil.isEmpty(userInfo)) {
            String jsessionId = null;
            Cookie[] cookies = request.getCookies();
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("jsessionId")) {
                    jsessionId = cookie.getValue();
                    break;
                }
            }
            if (!StringUtil.isEmpty(jsessionId)) {
                userInfo = userService.getUserLoginInfoByJsessionId(jsessionId);

            }
            if (StringUtil.isEmpty(userInfo)) {
                logger.warn("LoginInfoInterceptor#user not login");
                response.reset();
                response.setContentType("application/json");
                response.setCharacterEncoding("utf-8");
                response.getWriter()
                        .print(new Gson().toJson(new GenericResponse<String>(AgentsellerErrorCode.NO_LOGIN)));
                return false;
            }
            request.getSession().setAttribute("user_info", userInfo);
            return true;
        }

        return true;
    }

}
