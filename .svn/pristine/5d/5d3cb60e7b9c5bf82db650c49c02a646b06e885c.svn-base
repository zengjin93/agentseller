package com.baidu.agentseller.base.util.common;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Map处理帮助类
 * 
 * @author zengwen
 * @version $Id: MapUtil.java, v 0.1 2014年3月20日 下午2:32:56 Exp $
 */
public class MapUtil {

    /**
     * 将类对象的非空元素转换为key=value串，以&连接，按key升序排列
     */
    public static String objToStr(Object object, String...excludedKeys) {
        return mapToStr(objToMap(object, excludedKeys), excludedKeys);
    }

    /**
     * 将map非空元素转换为key=value串，以&连接，按key升序排列
     */
    public static String mapToStr(Map<String, String> params, String...excludedKeys) {
        if (params == null) {
            return null;
        }
        for (String excludedKey : excludedKeys) {
            if (params.containsKey(excludedKey)) {
                params.remove(excludedKey);
            }
        }
        StringBuffer content = new StringBuffer();
        List<String> keys = new ArrayList<String>(params.keySet());
        Collections.sort(keys);
        for (int i = 0; i < keys.size(); i++) {
            String key = keys.get(i);
            String value = params.get(key);
            content.append((i == 0 ? "" : "&") + key + "=" + value);
        }
        return content.toString();
    }

    /**
     * 对象转换成map,并排序
     */
    public static Map<String, String> objToMap(Object object, String...excludedKeys) {
        Map<String, String> map = new TreeMap<String, String>();
        try {
            Class<?> c = Class.forName(object.getClass().getName());
            Method[] m = c.getMethods();
            for (int i = 0; i < m.length; i++) {
                String method = m[i].getName();
                if (method.startsWith("get")) {
                    Object value = m[i].invoke(object);
                    if (value != null) {
                        if (value instanceof String || value instanceof Boolean || value instanceof Long
                                || value instanceof Integer) {
                            String key = method.substring(3);
                            key = key.substring(0, 1).toLowerCase() + key.substring(1);
                            map.put(key, String.valueOf(value));
                        } // TODO list and map
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        for (String excludedKey : excludedKeys) {
            if (map.containsKey(excludedKey)) {
                map.remove(excludedKey);
            }
        }
        return map;
    }

}
