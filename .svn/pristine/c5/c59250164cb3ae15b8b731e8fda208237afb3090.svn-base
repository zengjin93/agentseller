package com.baidu.agentseller.base.util.common;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import com.baidu.agentseller.base.util.common.constants.CommonConstants;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser.Feature;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

/**
 * JSON工具类
 * 
 * @author lianzerong
 */
public class JsonUtil {
    /** logger */
    private final static Logger logger = LoggerFactory.getLogger(JsonUtil.class);

    /**
     * 把JSON字符串转换为指定的对象
     * 
     * @param jsonString
     * @param valueType
     * @return
     */
    public static <T> T transferToObj(String jsonString, Class<T> valueType) {
        if (StringUtils.isBlank(jsonString)) {
            throw new IllegalArgumentException("[Assertion failed] - the object argument must be blank");
        }

        T value = null;
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            mapper.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);
            mapper.configure(SerializationFeature.INDENT_OUTPUT, false);
            mapper.configure(Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true);
            value = mapper.readValue(jsonString, valueType);
        } catch (JsonParseException e) {
            logger.error("jsonString=[" + jsonString + "]", e);
        } catch (JsonMappingException e) {
            logger.error("jsonString=[" + jsonString + "]", e);
        } catch (IOException e) {
            logger.error("jsonString=[" + jsonString + "]", e);
        }

        return value;
    }

    /**
     * 将SON字符串转换为指定的对象，如果出现异常，不打印ERROR日志
     * 
     * @param jsonString
     * @param valueType
     * @return
     */
    public static <T> T transferToObjWithOutErrorLog(String jsonString, Class<T> valueType) {
        if (StringUtils.isBlank(jsonString)) {
            return null;
        }

        T value = null;
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            mapper.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);
            mapper.configure(SerializationFeature.INDENT_OUTPUT, false);
            mapper.configure(Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true);
            value = mapper.readValue(jsonString, valueType);
        } catch (JsonParseException e) {
            logger.error("transferToObjWithOutErrorLog exception: jsonString=" + jsonString + ", class=" + valueType,
                    e);
        } catch (JsonMappingException e) {
            logger.error("transferToObjWithOutErrorLog exception: jsonString=" + jsonString + ", class=" + valueType,
                    e);
        } catch (IOException e) {
            logger.error("transferToObjWithOutErrorLog exception: jsonString=" + jsonString + ", class=" + valueType,
                    e);
        } catch (Exception e) {
            logger.error("transferToObjWithOutErrorLog exception: jsonString=" + jsonString + ", class=" + valueType,
                    e);
        }

        return value;
    }

    /**
     * 把指定的对象生成JSON字符串
     *
     * @param value
     * @return
     */
    public static <T> String transfer2JsonString(T value) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRITE_ENUMS_USING_TO_STRING, true);

        StringWriter sw = new StringWriter();
        JsonGenerator gen;
        try {
            gen = new JsonFactory().createGenerator(sw);
            mapper.writeValue(gen, value);
            gen.close();
        } catch (IOException e) {
            logger.error("value=[" + value + "]", e);

        }

        return sw.toString();
    }

    /**
     * 把指定的对象生成JSON字符串
     *
     * @param value
     * @return
     */
    public static <T> String transfer2JsonStringWithoutNull(T value) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRITE_ENUMS_USING_TO_STRING, true);
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        StringWriter sw = new StringWriter();
        JsonGenerator gen;
        try {
            gen = new JsonFactory().createGenerator(sw);
            mapper.writeValue(gen, value);
            gen.close();
        } catch (IOException e) {
            logger.error("value=[" + value + "]", e);

        }
        return sw.toString();
    }

    public static Map<String, String> transferObject2MapWithoutNull(Object obj) {
        String json = transfer2JsonStringWithoutNull(obj);
        return readJson2Map(json);
    }

    /**
     *
     * @param map
     * @return
     */
    @SuppressWarnings("rawtypes")
    public static String writeMap2JsonString(Map map) {
        if (CollectionUtils.isEmpty(map)) {
            return "";
        }
        String jsonString = "";
        try {
            ObjectMapper mapper = new ObjectMapper();
            jsonString = mapper.writeValueAsString(map);
        } catch (JsonProcessingException e) {
            logger.error("map=[" + map + "]", e);
        }

        return jsonString;
    }

    /**
     *
     * @param jsonString
     * @return
     */
    @SuppressWarnings("unchecked")
    public static Map<String, String> readJson2Map(String jsonString) {
        if (!jsonFormatCheck(jsonString)) {
            return null;
        }
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(jsonString, Map.class);
        } catch (JsonParseException e) {
            logger.error("jsonString=[" + jsonString + "]", e);
        } catch (JsonMappingException e) {
            logger.error("jsonString=[" + jsonString + "]", e);
        } catch (IOException e) {
            logger.error("jsonString=[" + jsonString + "]", e);
        }

        return null;
    }

    /**
     *
     * @param jsonString
     * @return
     */
    @SuppressWarnings("unchecked")
    public static Map<String, String> readJson2MapWithoutError(String jsonString) {
        if (!jsonFormatCheck(jsonString)) {
            return null;
        }
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(jsonString, Map.class);
        } catch (JsonParseException e) {
            logger.warn("readJson2MapWithoutError exception, jsonString=[" + jsonString + "]", e);
        } catch (JsonMappingException e) {
            logger.warn("readJson2MapWithoutError exception, jsonString=[" + jsonString + "]", e);
        } catch (Exception e) {
            logger.warn("readJson2MapWithoutError exception, jsonString=[" + jsonString + "]", e);
        }

        return null;
    }

    /**
     * 对于复杂集合类的转换 例如：
     * <p>
     * <li>1.List<JavaBean>: 调用方法是：readJson2Map(jsonString,List.class,JavaBean.class);</li>
     * <li>2.Map<JavaBean1,JavaBean2>:调用方法是:readJson2Map(jsonString,Map.class,JavaBean1.class,JavaBean2.class);</li>
     * </p>
     *
     * @param jsonString
     * @return
     */
    public static <T> T readJson2Collection(String jsonString, Class<?> parametrized, Class<?>...parameterClasses) {
        if (StringUtils.isBlank(jsonString)) {
            return null;
        }

        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            mapper.enable(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_AS_NULL);
            JavaType javaType = mapper.getTypeFactory().constructParametricType(parametrized, parameterClasses);
            return mapper.<T> readValue(jsonString, javaType);
        } catch (JsonParseException e) {
            logger.error("jsonString=[" + jsonString + "]", e);
        } catch (JsonMappingException e) {
            logger.error("jsonString=[" + jsonString + "]", e);
        } catch (IOException e) {
            logger.error("jsonString=[" + jsonString + "]", e);
        }

        return null;
    }

    @SuppressWarnings("unchecked")
    public static <T> List<T> readJson2List(String jsonString, Class<T> clsT) {
        if (StringUtils.isBlank(jsonString)) {
            return null;
        }
        List<T> list = null;
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.configure(Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true);
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            JavaType javaType = objectMapper.getTypeFactory().constructParametricType(ArrayList.class, clsT);
            list = (List<T>) objectMapper.readValue(jsonString, javaType);
        } catch (Exception e) {
            logger.error("readJson2List exception: jsonString=" + jsonString + ", class=" + clsT, e);
        }
        return list;

    }

    public static boolean jsonFormatCheck(String str) {

        return StringUtils.startsWith(str, CommonConstants.LEFT_BRACE);
    }
}
