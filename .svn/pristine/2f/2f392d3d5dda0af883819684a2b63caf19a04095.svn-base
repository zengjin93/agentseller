/**
 * Copyright 2013-2013 baidu.com
 */
package com.baidu.agentseller.base.util.log.digest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.baidu.agentseller.base.util.common.Conventions;
import com.baidu.agentseller.base.util.common.context.ServiceContextHolder;
import com.baidu.agentseller.base.util.common.profile.ThreadProfiler;

/**
 * 用于Web的摘要日志拦截器 需要Servlet 3.0以上方可使用
 * 
 * @author dingxuefeng
 */
public class WebDigestLogHandlerInterceptor extends HandlerInterceptorAdapter {

    private static final String headType = "X_Real_Cost";

    private Logger logger;
    private ThreadLocal<WebDigestLogTimer> timer = new ThreadLocal<WebDigestLogTimer>();
    private boolean printArguments = true;
    private boolean printResults = true;
    private long slowDumpThreshold = 300;

    /**
     * 指定拦截器中的使用的日志
     */
    public WebDigestLogHandlerInterceptor(String loggerName) {
        logger = LoggerFactory.getLogger(loggerName);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.springframework.web.servlet.handler.HandlerInterceptorAdapter#preHandle(javax.servlet.http.
     * HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.Object)
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        WebDigestLogAdditionalInfoUtil.clear();
        WebDigestLogTimer logTimer = new WebDigestLogTimer();
        logTimer.setBeginTime(System.currentTimeMillis());
        String handlerName = extractHandlerName(handler);
        ThreadProfiler.start("Invoke " + handlerName);
        timer.remove();
        timer.set(logTimer);
        return true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.springframework.web.servlet.handler.HandlerInterceptorAdapter#postHandle(javax.servlet.http.
     * HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.Object,
     * org.springframework.web.servlet.ModelAndView)
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
            ModelAndView modelAndView) throws Exception {
        timer.get().setProcessEndTime(System.currentTimeMillis());
        String realCost = Long.toString(getSubtractedTime(timer.get().getBeginTime(), timer.get().getProcessEndTime()));
        response.addHeader(headType, realCost);
    }

    /**
     * 摘要日志格式：
     * 
     * [(源请求IP,客户真实IP)(METHOD, URI, 处理方法, 处理结果, 总时间, 处理时间, 渲染时间)(参数)(Status,结果)]
     * 
     * @see org.springframework.web.servlet.handler.HandlerInterceptorAdapter#afterCompletion(javax.servlet.http.HttpServletRequest,
     *      javax.servlet.http.HttpServletResponse, java.lang.Object, java.lang.Exception)
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
            throws Exception {
        String handlerName = extractHandlerName(handler);
        WebDigestLogTimer logTimer = timer.get();
        logTimer.setRenderEndTime(System.currentTimeMillis());
        ThreadProfiler.release();
        ThreadProfiler.logIfSlow(slowDumpThreshold);

        String result = getResultFlag(response, ex);
        logger.info("[({},{})({},{},{},{},{}ms,{}ms,{}ms)({})({},{})]",
                StringUtils.defaultString(ServiceContextHolder.get(Conventions.REAL_IP_HEADER), "-"),
                StringUtils.defaultString(ServiceContextHolder.get(Conventions.CLIENT_IP_HEADER), "-"),
                request.getMethod(), request.getServletPath(), handlerName, result,
                getSubtractedTime(logTimer.getBeginTime(), logTimer.getRenderEndTime()),
                getSubtractedTime(logTimer.getBeginTime(), logTimer.getProcessEndTime()),
                getSubtractedTime(logTimer.getProcessEndTime(), logTimer.getRenderEndTime()), getArgumentsString(),
                getResponseStatus(response), getResultsString());
        WebDigestLogAdditionalInfoUtil.setPrintFlag(true);
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * org.springframework.web.servlet.handler.HandlerInterceptorAdapter#afterConcurrentHandlingStarted(javax.servlet.
     * http.HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.Object)
     */
    @Override
    public void afterConcurrentHandlingStarted(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        // 异步Controller 才会调用这个方法,
        // 调用的流程是:异步请求线程先支调用preHandle、然后执行afterConcurrentHandlingStarted[所以此处应该调用afterCompletion],然后线程就去干别的事了
        // 异步线程完成之后 执行preHandle、postHandle、afterCompletion[所以缺点是统计出来的时间根本不准确.]
        // 因为请求的开始是线程A, 而请求的结束时线程B,两个线程保存的ThreadLocal值无法共享,所以 统计时间不准确.
        // 建议不要用异步Controller,意义不大. 线程不够就上多台机器呗.
        this.afterCompletion(request, response, handler, null);
    }

    /**
     * 获得处理器名称
     */
    private String extractHandlerName(Object handler) {
        String handlerName = getRealClassName(handler.getClass().getName());
        if (handler.getClass() == HandlerMethod.class) {
            HandlerMethod method = (HandlerMethod) handler;
            handlerName =
                    getRealClassName(method.getBean().getClass().getSimpleName()) + "." + method.getMethod().getName();
        }
        return handlerName;
    }

    /**
     * 处理被CGLIB增强过的类名，例如：CustomerServicesController$$EnhancerByCGLIB$$339ba907
     */
    private String getRealClassName(String className) {
        if (StringUtils.contains(className, "$$Enhancer")) {
            return StringUtils.split(className, "$$")[0];
        }
        return className;
    }

    /**
     * 如果上下文设置本次执行结果为失败，则返回N 如果有异常，直接判断为N，响应结果码>=500，也判断为N
     */
    private String getResultFlag(HttpServletResponse response, Exception ex) {
        if (!WebDigestLogAdditionalInfoUtil.getResult()) {
            return "N";
        }
        String result = ex == null ? "Y" : "N";
        if (StringUtils.equals("Y", result) && response != null
                && response.getStatus() >= HttpServletResponse.SC_INTERNAL_SERVER_ERROR) {
            result = "N";
        }
        return result;
    }

    private long getSubtractedTime(Long begin, Long end) {
        if (begin != null && end != null) {
            return end - begin;
        } else {
            return -1;
        }
    }

    private String getResponseStatus(HttpServletResponse response) {
        String status = "-";
        if (response != null) {
            status = Integer.toString(response.getStatus());
        }
        return status;
    }

    private Object getArgumentsString() {
        if (printArguments) {
            return StringUtils.join(WebDigestLogAdditionalInfoUtil.getParameters(), ",");
        } else {
            return "-";
        }
    }

    private Object getResultsString() {
        if (printResults) {
            return StringUtils.join(WebDigestLogAdditionalInfoUtil.getResults(), ",");
        } else {
            return "-";
        }
    }

    public void setPrintArguments(boolean printArguments) {
        this.printArguments = printArguments;
    }

    public void setPrintResults(boolean printResults) {
        this.printResults = printResults;
    }

    public void setSlowDumpThreshold(long slowDumpThreshold) {
        this.slowDumpThreshold = slowDumpThreshold;
    }
}
