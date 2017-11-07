/**
 * Copyright 2013-2013 baidu.com
 */
package com.baidu.agentseller.base.util.log.mdc;

import java.util.Random;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.MDC;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.baidu.agentseller.base.util.common.Conventions;

/**
 * 建立日志MDC上下文属性的拦截器
 *
 * @author dingxuefeng
 */
public class WebLogMdcHandlerInterceptor extends HandlerInterceptorAdapter {
    /**
     * LogId一般有前端的负载生成，比如Nginx或Lighttpd
     */
    private boolean generateLogId = false;

    /*
     * (non-Javadoc)
     * 
     * @see org.springframework.web.servlet.handler.HandlerInterceptorAdapter#preHandle(javax.servlet.http
     * .HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.Object)
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        String ctxLogId = null;
        String ctxOpId = null;
        if (StringUtils.isNotBlank(request.getHeader(Conventions.LOG_ID_HEADER))) {
            ctxLogId = request.getHeader(Conventions.LOG_ID_HEADER);
        } else if (generateLogId) {
            ctxLogId = getLodId();
        }
        ctxOpId = UUID.randomUUID().toString();
        MDC.put(Conventions.CTX_LOG_ID_MDC, ctxLogId + "," + ctxOpId);

        if (StringUtils.isNotBlank(request.getHeader(Conventions.SRC_SYS_HEADER))) {
            MDC.put(Conventions.CTX_SRC_SYS_MDC, request.getHeader(Conventions.SRC_SYS_HEADER));
        }

        return true;
    }

    private static String getLodId() {
        Random random = new Random();
        String rs1 = String.valueOf(random.nextInt(10000));
        String rs2 = String.valueOf(random.nextInt(10000));
        return rs1 + rs2;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.springframework.web.servlet.handler.HandlerInterceptorAdapter#afterCompletion(javax.servlet.http
     * .HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.Object, java.lang.Exception)
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
            throws Exception {
        MDC.clear();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.springframework.web.servlet.handler.HandlerInterceptorAdapter#afterConcurrentHandlingStarted(javax
     * .servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.Object)
     */
    @Override
    public void afterConcurrentHandlingStarted(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        // 异步Controller 才会调用这个方法,
        // 调用的流程是:异步请求线程先支调用preHandle、然后执行afterConcurrentHandlingStarted[所以此处应该调用[afterCompletion进行MDC的日志清理],然后线程就去干别的事了
        // 异步线程完成之后 执行preHandle、postHandle、afterCompletion
        // 建议不要用异步Controller,意义不大. 线程不够就上多台机器呗.
        afterCompletion(request, response, handler, null);
    }

    public void setGenerateLogId(boolean generateLogId) {
        this.generateLogId = generateLogId;
    }
}
