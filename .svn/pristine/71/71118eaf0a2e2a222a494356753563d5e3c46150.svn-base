package com.baidu.agentseller.base.util.common;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.imageio.ImageIO;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.WebUtils;

import com.baidu.agentseller.base.util.common.constants.CommonConstants;

/**
 * 
 * @author lianzerong
 */
public class HttpUtil {
    private static Logger logger = LoggerFactory.getLogger(HttpUtil.class);

    /**
     * 把请求参数转换为Map对象
     * 
     * @param request
     * @return
     */
    public static Map<String, String> copyRequestParameter2Map(HttpServletRequest request) {
        Map<String, String> parameterMap = new HashMap<String, String>();
        for (Entry<String, String[]> entry : request.getParameterMap().entrySet()) {
            if (entry != null) {
                String[] value = entry.getValue();
                if (value == null || value.length == 0 || StringUtils.isBlank(entry.getValue()[0])) {
                    parameterMap.put(entry.getKey(), "");
                } else {
                    parameterMap.put(entry.getKey(), entry.getValue()[0]);
                }
            }

        }
        return parameterMap;
    }

    /**
     * http请求回写
     * 
     * @param responseStr
     * @param response
     * @throws IOException
     */
    public static void writeResponse(String responseStr, HttpServletResponse response) {
        writeResponse(responseStr, response, "text/html;charset=GBK", "GBK");
    }

    public static String checkEncodingGBK(String encoding) {
        String defaultEncoding = "GBK";
        if (StringUtils.isBlank(encoding)) {
            return defaultEncoding;
        }
        try {
            Charset charset = Charset.forName(encoding);
            if (charset == null) {
                return defaultEncoding;
            }
            return encoding;
        } catch (Exception e) {
            logger.info("check Encoding error ! charset = " + encoding);
            return defaultEncoding;
        }
    }

    /**
     * http请求回写
     * 
     * @param responseStr
     * @param response
     * @throws IOException
     */
    public static void writeResponseJson(String responseStr, HttpServletResponse response) {
        writeResponse(responseStr, response, "application/json;charset=GBK", "GBK");
    }

    /**
     * http请求回写
     * 
     * @param responseStr
     * @param response
     * @throws IOException
     */
    public static void writeResponseJson(String responseStr, HttpServletResponse response, String encoding) {
        writeResponse(responseStr, response, "application/json;charset=" + encoding, encoding);
    }

    /**
     * 返回图片
     * 
     * @param image
     * @param response
     * @throws IOException
     */
    public static void writeResponseImage(BufferedImage image, HttpServletResponse response) {
        response.setHeader("Cache-Control", "no-cache");
        response.setContentType("image/jpeg");
        try {
            ImageIO.write(image, "JPEG", response.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * http请求回写
     * 
     * @param responseStr
     * @param response
     * @throws IOException
     */
    public static void writeResponse(String responseStr, HttpServletResponse response, String contentType,
            String encoding) {
        try {
            response.setContentType(contentType);
            response.setCharacterEncoding(encoding);
            response.setHeader("Cache-Control", "no-cache");
            PrintWriter out = response.getWriter();
            out.write(responseStr);
        } catch (Exception e) {
            logger.error("http write  Exception,return response=" + responseStr, e);
        }
    }

    /**
     * 读取请求参数
     * 
     * @param request
     * @param name
     * @return
     */
    public static String getRequestParameter(HttpServletRequest request, String name) {
        Object valueObj = request.getAttribute(name);
        if (null != valueObj)
            return (String) valueObj;

        return request.getParameter(name);
    }

    /**
     * 从session或指定参数的值
     * 
     * @param request
     * @param name
     * @return
     */
    public static String getSessionParameter(HttpServletRequest request, String name) {

        return getSessionParameter(request.getSession(), name);
    }

    /**
     * 从session或指定参数的值
     * 
     * @param session
     * @param name
     * @return
     */
    public static String getSessionParameter(HttpSession session, String name) {
        Object valueObj = session.getAttribute(name);
        if (null != valueObj)
            return (String) valueObj;

        return null;
    }

    public static String fromRequestString(HttpServletRequest request) {
        String currentUrl = ServletUriComponentsBuilder.fromRequest(request).build().normalize().getPath().toString();
        currentUrl += fromRequestParams(request);

        return currentUrl;
    }

    /**
     * 把请求过来的参数拼装成get请求的参数字符串，例如：?accessChannel=WAP&subBizType=1001001&bizType=1001000
     * 
     * @param request
     * @return
     */
    public static String fromRequestParams(HttpServletRequest request) {
        Map<String, String> paramMap = copyRequestParameter2Map(request);
        return formatParams(paramMap);
    }

    public static String formatParams(Map<String, String> paramMap) {
        if (!CollectionUtils.isEmpty(paramMap)) {
            String urlParams = CommonConstants.QUESTION_MARK;
            for (Entry<String, String> entry : paramMap.entrySet()) {
                urlParams +=
                        entry.getKey() + CommonConstants.EQUAL + paramMap.get(entry.getKey()) + CommonConstants.AND;
            }
            return StringUtils.substringBeforeLast(urlParams, CommonConstants.AND);
        }
        return "";
    }

    public static String formatUrl(String url, Map<String, String> paramMap) {
        StringBuffer sbUrl = new StringBuffer();
        if (StringUtils.isBlank(url)) {
            return "";
        }
        sbUrl.append(url);
        if (!CollectionUtils.isEmpty(paramMap)) {
            if (url.contains(CommonConstants.QUESTION_MARK)) {
                sbUrl.append(CommonConstants.AND);
            } else {
                sbUrl.append(CommonConstants.QUESTION_MARK);
            }
            for (Entry<String, String> entry : paramMap.entrySet()) {
                sbUrl.append(entry.getKey());
                sbUrl.append(CommonConstants.EQUAL);
                sbUrl.append(paramMap.get(entry.getKey()));
                sbUrl.append(CommonConstants.AND);
            }
            return StringUtils.substringBeforeLast(sbUrl.toString(), CommonConstants.AND);
        }
        return sbUrl.toString();
    }

    /**
     * <p>
     * 判断是否ajax请求
     * </p>
     * 
     * @param request
     * @return
     */
    public static boolean checkIsAjax(HttpServletRequest request) {
        String reqUri = request.getRequestURI();
        if (StringUtils.contains(reqUri, "/ajax")) {
            return true;
        }

        return (StringUtils.contains(getHeader(request, "accept"), "application/json")
                || StringUtils.contains(getHeader(request, "X-Requested-With"), "XMLHttpRequest"));
    }

    /**
     * <p>
     * 读取请求的头信息，指定名称的值
     * </p>
     * 
     * @param request
     * @param name
     * @return
     */
    public static String getHeader(HttpServletRequest request, String name) {

        return request.getHeader(name);
    }

    public static String getCookie(HttpServletRequest request, String cName) {
        Cookie cookie = WebUtils.getCookie(request, cName);
        if (null != cookie) {
            return cookie.getValue();
        }
        return null;
    }

    public static void addCookie(HttpServletResponse response, String cName, String cValue) {
        Cookie cookie = new Cookie(cName, cValue);
        cookie.setPath("/");
        response.addCookie(cookie);
    }

    public static void delCookie(HttpServletResponse response, String cName) {
        Cookie cookie = new Cookie(cName, null);
        cookie.setMaxAge(0);
        response.addCookie(cookie);
    }

}
