/*
 * Copyright 2013-2014 baidu.com
 */
package com.baidu.agentseller.base.util.log.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import com.baidu.agentseller.base.util.common.Conventions;
import com.baidu.agentseller.base.util.log.digest.WebDigestLogAdditionalInfoUtil;

/**
 * @author dingxuefeng
 */
public class ExceptionLogFilter implements Filter {
    private static final Logger logger = LoggerFactory.getLogger(ExceptionLogFilter.class);
    private static final Logger pageDigestLogger = LoggerFactory.getLogger("PAGE-DIGEST-LOGGER");
    private static final Logger serviceDigestLogger = LoggerFactory.getLogger("SERVICE-DIGEST-LOGGER");

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        WebDigestLogAdditionalInfoUtil.setPrintFlag(false);
        prepareMdc(request);
        long startTime = System.currentTimeMillis();
        try {
            chain.doFilter(request, response);
        } catch (Exception e) {
            logger.error("Exception occurred but no one catch it!", e);
            throw new RuntimeException(e);
        } finally {
            if (!WebDigestLogAdditionalInfoUtil.isLogPrinted()) {
                logDigest(request, response, startTime);
            }
        }
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void destroy() {
    }

    private void logDigest(ServletRequest request, ServletResponse response, long startTime) {
        Logger loggerLocal = null;
        long time = System.currentTimeMillis() - startTime;
        HttpServletRequest req = request instanceof HttpServletRequest ? (HttpServletRequest) request : null;
        HttpServletResponse resp = response instanceof HttpServletResponse ? (HttpServletResponse) response : null;

        if (req == null || resp == null) {
            logger.error("Can't convert Request of Response! The process time of this request is {}ms. request=[{}]",
                    time, request);
            return;
        }

        if (StringUtils.startsWith(req.getRequestURI(), Conventions.SERVICES_URL_PREFIX)) {
            loggerLocal = serviceDigestLogger;
        } else {
            loggerLocal = pageDigestLogger;
        }

        loggerLocal.info("[({},{})({},{},-,N,{}ms,-,-)(-)({})]", getFromHeader(req, Conventions.REAL_IP_HEADER),
                getFromHeader(req, Conventions.CLIENT_IP_HEADER), req.getMethod(), req.getRequestURI(), time,
                resp.getStatus());
    }

    private void prepareMdc(ServletRequest request) {
        HttpServletRequest req = request instanceof HttpServletRequest ? (HttpServletRequest) request : null;

        if (req == null) {
            return;
        }

        if (StringUtils.isNotBlank(req.getHeader(Conventions.LOG_ID_HEADER))) {
            MDC.put(Conventions.CTX_LOG_ID_MDC, req.getHeader(Conventions.LOG_ID_HEADER));
        }

        if (StringUtils.isNotBlank(req.getHeader(Conventions.SRC_SYS_HEADER))) {
            MDC.put(Conventions.CTX_SRC_SYS_MDC, req.getHeader(Conventions.SRC_SYS_HEADER));
        }
    }

    private String getFromHeader(HttpServletRequest req, String header) {
        if (req == null || StringUtils.isBlank(header)) {
            return "-";
        }
        return StringUtils.defaultString(req.getHeader(header), "-");
    }
}
