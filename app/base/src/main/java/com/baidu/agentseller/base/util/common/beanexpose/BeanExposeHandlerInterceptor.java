/*
 * Copyright 2013-2014 baidu.com
 */
package com.baidu.agentseller.base.util.common.beanexpose;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.baidu.agentseller.base.util.common.AppConfigUtil;

/**
 * 将一些SpringBean放入ModelAndView中，以便模板中能够方便地使用它们。 默认提供了一些基础的辅助类。
 *
 * @author dingxuefeng
 */
public class BeanExposeHandlerInterceptor extends HandlerInterceptorAdapter {
    private static final Logger logger = LoggerFactory.getLogger(BeanExposeHandlerInterceptor.class);

    private Map<String, Object> beanMap = new HashMap<String, Object>();

    public BeanExposeHandlerInterceptor() {
        beanMap.put("appConfigUtil", new AppConfigUtil());
        beanMap.put("stringUtils", new StringUtils());
        beanMap.put("stringEscapeUtils", new StringEscapeUtils());
        beanMap.put("numberUtils", new NumberUtils());
        beanMap.put("dateUtils", new DateUtils());
        beanMap.put("dateFormatUtils", new DateFormatUtils());

        beanMap.put("uriComponentsBuilder", ServletUriComponentsBuilder.newInstance());

        logger.debug("初始化BeanMap，目前其中内容如下：{}", this.beanMap);
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
            ModelAndView modelAndView) throws Exception {
        if (modelAndView != null) {
            modelAndView.addAllObjects(beanMap);
        } else if (beanMap != null) {
            logger.debug("ModelAndView为空，beanMap内容放入Request对象。");
            for (String key : beanMap.keySet()) {
                request.setAttribute(key, beanMap.get(key));
            }
        }
    }

    public void setBeanMap(Map<String, Object> beanMap) {
        this.beanMap.putAll(beanMap);
        logger.debug("自定义BeanMap，合并后的Map如下：{}", this.beanMap);
    }
}
