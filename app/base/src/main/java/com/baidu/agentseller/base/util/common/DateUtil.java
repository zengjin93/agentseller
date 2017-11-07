package com.baidu.agentseller.base.util.common;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.Period;
import org.joda.time.PeriodType;
import org.joda.time.format.DateTimeFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

/**
 * 日期工具类
 * 
 * @author zengwen
 * @version $Id: DateUtil.java, v 0.1 2014年2月19日 下午9:18:42 Exp $
 */
public class DateUtil extends org.apache.commons.lang3.time.DateUtils {
    /** logger */
    private final static Logger logger = LoggerFactory.getLogger(DateUtil.class);

    /** yyyyMMdd */
    public final static String SHORT_FORMAT = "yyyyMMdd";

    /** yyyyMMddHHmmss */
    public final static String LONG_FORMAT = "yyyyMMddHHmmss";

    /** yyyy-MM-dd */
    public final static String WEB_FORMAT = "yyyy-MM-dd";

    /** HHmmss */
    public final static String TIME_FORMAT = "HHmmss";

    /** yyyyMM */
    public final static String MONTH_FORMAT = "yyyyMM";

    /** yyyy-MM */
    public final static String WEB_MONTH_FORMAT = "yyyy-MM";

    /** yyyy年MM月dd日 */
    public final static String CHINA_FORMAT = "yyyy年MM月dd日";

    /** yyyy-MM-dd HH:mm:ss */
    public final static String LONG_WEB_FORMAT = "yyyy-MM-dd HH:mm:ss";

    /** yyyy-MM-dd HH:mm */
    public final static String LONG_WEB_FORMAT_NO_SEC = "yyyy-MM-dd HH:mm";

    public final static String LONG_WEB_FORMAT_NO_SEC_NO_YEAR = "MM-dd HH:mm";

    public final static String MONTH_DAY_FORMAT = "MM月dd日";

    /** yyyyMMddHHmmssSSS */
    public final static String ACCURATE_FORMAT = "yyyyMMddHHmmssSSS";

    /**
     * 日期增加
     * 
     * @param start 开始时间
     * @param minutes 分钟数
     * @return
     */
    public static Date minusMinutes(Date start, int minutes) {
        DateTime newDate = new DateTime(start).minusMinutes(minutes);
        return newDate.toDate();
    }

    /**
     * 日期对象解析成日期字符串基础方法，可以据此封装出多种便捷的方法直接使用
     * 
     * @param date 待格式化的日期对象
     * @param format 输出的格式
     * @return 格式化的字符串
     */
    public static String format(Date date, String format) {
        if (date == null || StringUtils.isBlank(format)) {
            return StringUtils.EMPTY;
        }
        return new DateTime(date).toString(format, Locale.SIMPLIFIED_CHINESE);
    }

    public static Date getYesterday() {
        return DateTime.now().minusDays(1).toDate();
    }

    public static Date getTomorrow() {
        return DateTime.now().plusDays(1).toDate();
    }

    /**
     * 格式化当前时间
     * 
     * @param format 输出的格式
     * @return
     */
    public static String formatCurrent(String format) {
        if (StringUtils.isBlank(format)) {
            return StringUtils.EMPTY;
        }
        return new DateTime().toString(format, Locale.SIMPLIFIED_CHINESE);
    }

    /**
     * 日期字符串解析成日期对象基础方法，可以在此封装出多种便捷的方法直接使用
     * 
     * @param dateStr 日期字符串
     * @param format 输入的格式
     * @return 日期对象
     * @throws ParseException
     */
    public static Date parse(String dateStr, String format) throws IllegalArgumentException {
        return DateTimeFormat.forPattern(format).withLocale(Locale.SIMPLIFIED_CHINESE).parseLocalDateTime(dateStr)
                .toDate();
    }

    /**
     * 日期字符串解析成日期对象基础方法，可以在此封装出多种便捷的方法直接使用
     * 
     * @param dateStr 日期字符串
     * @param format 输入的格式
     * @return 日期对象
     * @throws ParseException
     */
    public static Date parseLongFormat(String dateStr) throws IllegalArgumentException {
        int formatLength = StringUtils.length(DateUtil.LONG_FORMAT);
        dateStr = StringUtils.rightPad(dateStr, formatLength, "0");
        return parse(dateStr, LONG_FORMAT);
    }

    /**
     * 日期字符串解析成日期对象基础方法，可以在此封装出多种便捷的方法直接使用
     * 
     * @param dateStr 日期字符串 (yyyy-MM-dd HH:mm:ss)
     * @return 日期对象
     * @throws ParseException
     */
    public static Date parseLongWebFormat(String dateStr) throws IllegalArgumentException {

        return parse(dateStr, LONG_WEB_FORMAT);
    }

    /**
     * 日期字符串格式化基础方法，可以在此封装出多种便捷的方法直接使用
     * 
     * @param dateStr 日期字符串
     * @param formatIn 输入的日期字符串的格式
     * @param formatOut 输出日期字符串的格式
     * @return 已经格式化的字符串
     * @throws ParseException
     */
    public static String format(String dateStr, String formatIn, String formatOut) throws IllegalArgumentException {
        Date date = parse(dateStr, formatIn);
        return format(date, formatOut);
    }

    /**
     * 把日期对象按照<code>yyyyMMdd</code>格式解析成字符串
     * 
     * @param date 待格式化的日期对象
     * @return 格式化的字符串
     */
    public static String formatShort(Date date) {
        return format(date, SHORT_FORMAT);
    }

    /**
     * 把日期字符串按照<code>yyyyMMdd</code>格式，进行格式化
     * 
     * @param dateStr 待格式化的日期字符串
     * @param formatIn 输入的日期字符串的格式
     * @return 格式化的字符串
     */
    public static String formatShort(String dateStr, String formatIn) throws IllegalArgumentException {
        return format(dateStr, formatIn, SHORT_FORMAT);
    }

    /**
     * 把日期对象按照<code>yyyy-MM-dd</code>格式解析成字符串
     * 
     * @param date 待格式化的日期对象
     * @return 格式化的字符串
     */
    public static String formatWeb(Date date) {
        return format(date, WEB_FORMAT);
    }

    /**
     * 把日期字符串按照<code>yyyy-MM-dd</code>格式，进行格式化
     * 
     * @param dateStr 待格式化的日期字符串
     * @param formatIn 输入的日期字符串的格式
     * @return 格式化的字符串
     * @throws ParseException
     */
    public static String formatWeb(String dateStr, String formatIn) throws IllegalArgumentException {
        return format(dateStr, formatIn, WEB_FORMAT);
    }

    /**
     * 把日期对象按照<code>yyyyMM</code>格式解析成字符串
     * 
     * @param date 待格式化的日期对象
     * @return 格式化的字符串
     */
    public static String formatMonth(Date date) {

        return format(date, MONTH_FORMAT);
    }

    /**
     * 把日期对象按照<code>HHmmss</code>格式解析成字符串
     * 
     * @param date 待格式化的日期对象
     * @return 格式化的字符串
     */
    public static String formatTime(Date date) {
        return format(date, TIME_FORMAT);
    }

    /**
     * 获取yyyyMMddHHmmss+n位随机数格式的时间戳
     * 
     * @param n 随机数位数
     * @return
     */
    public static String getTimestamp() {
        return formatCurrent(LONG_FORMAT);
    }

    /**
     * 根据日期格式返回昨日日期
     * 
     * @param format
     * @return
     */
    public static String getYesterdayDate(String format) {
        return format(DateTime.now().minusDays(1).toDate(), format);
    }

    /**
     * 验证系统当前时间，是否在starDate与endDate之间(时间必须是毫秒级别的)
     * 
     * @param startDate
     * @param endDate
     * @return
     */
    public static boolean isBetweenStarAndEndDate(Date startDate, Date endDate) {
        Assert.notNull(startDate);
        Assert.notNull(endDate);

        return isBetweenStarAndEndDate(startDate, endDate, Calendar.MILLISECOND);
    }

    /**
     * 验证系统当前时间，是否在starDate与endDate之间
     * 
     * @param startDate
     * @param endDate
     * @param field =[Calendar.MILLISECOND 毫秒级别] =[Calendar.DAY 比较到天] =[Calendar.YEAR 比较到年]
     * @return
     */
    public static boolean isBetweenStarAndEndDate(Date startDate, Date endDate, int field) {
        Assert.notNull(startDate);
        Assert.notNull(endDate);
        // 系统当前时间
        Date date = truncate(new Date(), field);

        return isBetweenStarAndEndDate(date, startDate, endDate);
    }

    /**
     * 验证date是否在starDate与endDate之间
     * <p>
     * <li>true:date[20140603],startDate[20140601],endDate[20140606]</li>
     * <li>false:date[20140607],startDate[20140601],endDate[20140606]</li>
     * <li>false:date[20140531],startDate[20140601],endDate[20140606]</li>
     * </p>
     * 
     * @param date
     * @param startDate
     * @param endDate
     * @return
     */
    public static boolean isBetweenStarAndEndDate(Date date, Date startDate, Date endDate) {
        Assert.notNull(date);
        Assert.notNull(startDate);
        Assert.notNull(endDate);

        return !(date.getTime() < startDate.getTime() || date.getTime() > endDate.getTime());
    }

    /**
     * <p>
     * 获取日期
     * <li>1.例如:2014-09-18 16:15:18 = 18</li>
     * <li>2.例如:2014-09-11 16:18:18 = 11</li>
     * </p>
     * 
     * @param date
     * @return
     */
    public static String getDate(Date date) {

        return format(date, "dd");
    }

    /**
     * 格式化字符串，将yyyy-mm-dd hh:mm:ss 变为 yyyy-mm-dd hh:mm
     * 
     * @param dateStr
     * @return
     */
    public static String format(String dateStr) {
        if (StringUtils.isBlank(dateStr)) {
            return "";
        }
        String resultStr = format(dateStr, DateUtil.LONG_WEB_FORMAT, DateUtil.LONG_WEB_FORMAT_NO_SEC);
        resultStr = StringUtils.replace(StringUtils.trim(resultStr), " ", "<br/>");
        return resultStr;
    }

    /**
     * 格式化字符串，yyyy-mm-dd hh:mm:ss
     * 
     * @param dateStr
     * @return
     */
    public static String formatLongWeb(Date date) {
        return format(date, LONG_WEB_FORMAT);
    }

    /**
     * 格式化字符串，yyyy-mm-dd hh:mm
     * 
     * @param dateStr
     * @return
     */
    public static String formatLongWebNoSec(Date date) {
        return format(date, LONG_WEB_FORMAT_NO_SEC);
    }

    /**
     * <p>
     * 传人的时间与当前时间相比是否已经过了n小时了(n必须是正整数)
     * <li>example:</li>
     * <li>beforeNowHour("2015-07-23 5:00:00",2),now="2015-07-23 5:00:00", return true</li>
     * <li>beforeNowHour(null,2), return false</li>
     * </p>
     * 
     * @param date
     * @param n
     * @return
     */
    public static boolean beforeNowHour(Date date, int n) {
        if (null == date) {
            return false;
        }

        if (n == 0) {
            return isSameInstant(date, new Date());
        }

        if (n < 0) {
            n = Math.abs(n);
        }

        return addHours(new Date(), -n).after(date);
    }

    /**
     * <p>
     * 将Date格式化，返回 月.日 如：2015-08-17，返回8.17
     * </p>
     * 
     * @param date
     * @return
     */
    @SuppressWarnings("deprecation")
    public static String formatMD(Date date) {
        try {
            return String.valueOf(date.getMonth() + 1).concat("月").concat(String.valueOf(date.getDate())).concat("日");
        } catch (Exception e) {
            logger.error("", e, date);
            return "";
        }

    }

    /**
     * 转换成Unix时间戳(Unix timestamp)
     * 
     * @param dateStr
     * @return
     */
    public static String toUnixTime(String dateStr) {
        SimpleDateFormat df = new SimpleDateFormat(LONG_WEB_FORMAT);
        String unix = "";
        try {
            unix = df.parse(dateStr).getTime() / 1000 + "";
        } catch (ParseException e) {
            logger.error("", e, dateStr);
            return "";
        }
        return unix;
    }

    /**
     * 转换成Unix时间戳(Unix timestamp)
     * 
     * @param date
     * @return
     */
    public static String toUnixTime(Date date) {
        String unix = "";
        try {
            unix = date.getTime() / 1000 + "";
        } catch (Exception e) {
            logger.error("", e, date);
            return "";
        }
        return unix;
    }

    /**
     * 将Unix时间戳转换成指定的时间格式
     * 
     * @param unixTimeStamp
     * @param format
     * @return
     */
    public static String convertUnixTimeToFormat(String unixTime, String format) {
        if (StringUtils.isBlank(unixTime)) {
            return "";
        }
        try {
            Long timestamp = Long.parseLong(unixTime) * 1000;
            String date = new SimpleDateFormat(format).format(new Date(timestamp));
            return date;
        } catch (Exception e) {
            logger.warn("convertUnixTimeToFormat exception, unixTime=[" + unixTime + "], format=[" + format + "]", e);

            return "";
        }
    }

    /**
     * 将Unix时间戳转换成Date
     * 
     * @param unixTime
     * @return
     */
    public static Date convertUnixTimeToDate(String unixTime) {
        try {
            Long timestamp = Long.parseLong(unixTime) * 1000;
            return new Date(timestamp);
        } catch (Exception e) {
            logger.warn("convertUnixTimeToDate exception, unixTime=[" + unixTime + "]", e);
            return null;
        }
    }

    public static int getDifferDays(DateTime date1, DateTime date2) {
        Period period = new Period(date1, date2, PeriodType.days());
        return period.getDays();
    }

    /**
     * 获取days天之前的的开始时间
     * 
     * 
     */
    public static String getBerforeDayBeginDate(int days) {
        String date = format(DateTime.now().minusDays(days).toDate(), LONG_WEB_FORMAT);
        StringBuffer beforeDayDate = new StringBuffer();
        beforeDayDate.append(date.substring(0, date.indexOf(" ") + 1)).append("00:00:00");
        return beforeDayDate.toString();
    }

    public static void main(String[] args) {
        DateTime time = new DateTime(new Date());
        DateTime tim2 = new DateTime(parse("2016-07-14", WEB_FORMAT));
        System.out.println(getDifferDays(time, tim2));
    }

}
