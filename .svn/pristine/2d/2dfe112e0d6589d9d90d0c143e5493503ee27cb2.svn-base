package com.baidu.agentseller.base.util.common.json;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * JSON格式转换器。
 * 
 * @author guzhanglei
 */
public class JsonUtil {

    /**
     * 将json串转换为响应对象。
     */
    public static <T> T toSimpleObj(String jsonString, Class<T> targetClass) {
        if (jsonString == null) {
            return null;
        }

        JsonReader reader = new JsonReader();
        Object rootObj = reader.read(jsonString);
        if (rootObj instanceof Map<?, ?>) {
            try {
                T target = targetClass.newInstance();
                Map<?, ?> rootJson = (Map<?, ?>) rootObj;
                for (Object key : rootJson.keySet()) {
                    Object value = rootJson.get(key);
                    setElement(target, key, value);
                }
                return target;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return null;
    }

    /**
     * 将以&连接的key=value串转换为类对象
     */
    public static <T> T strToObj(String str, Class<T> targetClass) {
        if (str == null || str.isEmpty()) {
            return null;
        }
        String[] elements = str.split("&");
        if (elements.length == 0) {
            return null;
        }

        try {
            T target = targetClass.newInstance();
            for (String ele : elements) {
                String[] keyValue = ele.split("=");
                String key = keyValue[0];
                String value = keyValue[1];
                setElement(target, key, value);
            }
            return target;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static <T> void setElement(T target, Object key, Object value) throws NoSuchMethodException,
            IllegalAccessException, InvocationTargetException {
        Class<?> orginalClass = target.getClass();
        // 查询合适的set方法，包括父类
        while (!orginalClass.equals(Object.class)) {
            Field[] fields = orginalClass.getDeclaredFields();
            for (Field field : fields) {
                if (field.getName().equals(key)) {
                    Method setMethod = orginalClass.getMethod("set" + firstUpper(field.getName()), field.getType());
                    Class<?> paramClass = setMethod.getParameterTypes()[0];
                    if (String.class.isAssignableFrom(paramClass)) {
                        setMethod.invoke(target, String.valueOf(value));
                    } else if (Boolean.class.isAssignableFrom(paramClass)) {
                        setMethod.invoke(target, Boolean.parseBoolean(String.valueOf(value)));
                    } else if (Long.class.isAssignableFrom(paramClass)) {
                        setMethod.invoke(target, Long.parseLong(String.valueOf(value)));
                    } else if (Integer.class.isAssignableFrom(paramClass)) {
                        setMethod.invoke(target, Integer.parseInt(String.valueOf(value)));
                    } // TODO list and Map
                    return;
                }
            }
            orginalClass = orginalClass.getSuperclass();
        }
    }

    private static String firstUpper(String s) {
        String first = s.substring(0, 1).toUpperCase();
        String rest = s.substring(1, s.length());
        return new StringBuffer(first).append(rest).toString();
    }

}
