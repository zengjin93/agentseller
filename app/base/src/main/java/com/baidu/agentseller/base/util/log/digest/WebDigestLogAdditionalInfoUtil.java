/**
 * Copyright 2013-2013 baidu.com
 */
package com.baidu.agentseller.base.util.log.digest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 用于保存Web摘要日志参数的辅助类
 * 
 * @author dingxuefeng
 */
public class WebDigestLogAdditionalInfoUtil {
    private static final ThreadLocal<List<Object>> PARAMETERS = new ThreadLocal<List<Object>>();
    private static final ThreadLocal<List<Object>> RESULTS    = new ThreadLocal<List<Object>>();
    private static final ThreadLocal<Boolean>      FLAG       = new ThreadLocal<Boolean>();
    private static final ThreadLocal<Boolean>      RESULT     = new ThreadLocal<Boolean>();

    /**
     * 清空当前内容
     */
    public static void clear() {
        PARAMETERS.remove();
        PARAMETERS.set(new ArrayList<Object>());
        RESULTS.remove();
        RESULTS.set(new ArrayList<Object>());
        FLAG.remove();
        FLAG.set(false);
        RESULT.remove();
        RESULT.set(true);
    }

    /**
     * 向上下文中增加参数
     */
    public static void addParameters(Object... parameters) {
        putIntoThreadLocal(PARAMETERS, parameters);
    }

    /**
     * 获得上下文中的参数
     * 
     * @return 始终不会为null
     */
    public static List<Object> getParameters() {
        return getFromThreadLocal(PARAMETERS);
    }

    /**
     * 向上下文中增加结果
     */
    public static void addResults(Object... results) {
        putIntoThreadLocal(RESULTS, results);
    }

    /**
     * 获得上下文中的结果
     * 
     * @return 始终不会为null
     */
    public static List<Object> getResults() {
        return getFromThreadLocal(RESULTS);
    }

    /**
     * 设置日志是否打印的标记位
     */
    public static void setPrintFlag(boolean flag) {
        FLAG.set(flag);
    }

    /**
     * 判断日志是否已经打印
     */
    public static boolean isLogPrinted() {
        if (FLAG.get() == null || !FLAG.get()) {
            return false;
        }
        return true;
    }

    /**
     * 获取执行结果
     */
    public static boolean getResult() {
        if (RESULT.get() == null || !RESULT.get()) {
            return false;
        }
        return true;
    }

    /**
     *
     */
    public static void setResult(boolean result) {
        RESULT.set(result);
    }

    /**
     * 从ThreadLocal对象中取数据
     */
    private static List<Object> getFromThreadLocal(ThreadLocal<List<Object>> holder) {
        if (holder.get() != null) {
            return holder.get();
        } else {
            return new ArrayList<Object>();
        }
    }

    /**
     * 向ThreadLocal对象中填写数据
     */
    private static void putIntoThreadLocal(ThreadLocal<List<Object>> holder, Object... contents) {
        if (holder.get() != null) {
            holder.get().addAll(Arrays.asList(contents));
        } else {
            holder.set(new ArrayList<Object>(Arrays.asList(contents)));
        }
    }
}
