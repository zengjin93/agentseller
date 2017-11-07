package com.baidu.agentseller.service.integration.rest.context;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.baidu.agentseller.base.util.common.Conventions;
import com.baidu.agentseller.base.util.common.context.ServiceContextHolder;

/**
 * 服务上下文初始化拦截器
 *
 * @author dingxuefeng
 */
public class ServiceContextInterceptor extends HandlerInterceptorAdapter {
    /**
     * 构建一个新的上下文，填充以下内容：
     * 1. 应用名
     * 2. LOGID
     * 3. TXID
     * 4. X-REAL-IP
     * 5. CLIENTIP
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
      Object handler) throws Exception {
        ServiceContextHolder.init();
        addHeaderToContext(request, Conventions.TX_ID_KEY);
        addHeaderToContext(request, Conventions.REAL_IP_HEADER);
        addHeaderToContext(request, Conventions.CLIENT_IP_HEADER);
        return true;
    }

    private void addHeaderToContext(HttpServletRequest request, String header) {
        if (StringUtils.isNotBlank(request.getHeader(header))) {
            ServiceContextHolder.put(header, request.getHeader(header));
        }
    }
}
