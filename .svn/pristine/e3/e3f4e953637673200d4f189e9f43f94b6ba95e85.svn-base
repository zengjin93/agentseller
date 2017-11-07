/*
 * Copyright (C) 2016 Baidu, Inc. All Rights Reserved.
 */
package com.baidu.agentseller.base.util.common;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.apache.commons.lang3.math.NumberUtils;
import org.joda.money.CurrencyUnit;
import org.joda.money.Money;

/**
 * 金额对象辅助类
 *
 * @author dingxuefeng
 */
public class MoneyUtil {
    /**
     * 默认使用人民币，即CNY
     */
    public static final String DEFAULT_CURRENCY_CODE = "CNY";
    /**
     * 人民币的货币单位
     */
    public static final CurrencyUnit CNY_CURRENCY = CurrencyUnit.getInstance(DEFAULT_CURRENCY_CODE);

    /**
     * 根据传入的字符串获取Money对象，货币为人民币CNY
     *
     * @param cent 单位为分
     */
    public static Money getMoneyFromCent(String cent) {
        if (NumberUtils.isDigits(cent)) {
            return getMoneyFromCent(Long.parseLong(cent));
        } else {
            throw new IllegalArgumentException("传入的金额不是正确的数字类型");
        }
    }

    /**
     * 根据传入的数字获取Money对象，货币为人民币CNY
     *
     * @param cent 单位为分
     */
    public static Money getMoneyFromCent(long cent) {
        return Money.ofMinor(CNY_CURRENCY, cent);
    }

    /**
     * 根据传入的字符串获取Money对象，货币为人民币CNY
     *
     * @param yuan 单位为元，只接受两位小数
     */
    public static Money getMoneyFromYuan(String yuan) {
        return getMoneyFromYuan(yuan, RoundingMode.UNNECESSARY);
    }

    /**
     * 根据传入的字符串获取Money对象，货币为人民币CNY
     *
     * @param yuan 单位为元，最好是两位小数，超过位数会根据指定的方式进行处理
     * @param roundingMode 多于小数处理方式
     */
    public static Money getMoneyFromYuan(String yuan, RoundingMode roundingMode) {
        try {
            Double amount = Double.parseDouble(yuan);
            return getMoneyFromYuan(amount, roundingMode);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("传入的金额不是正确的金额类型", e);
        }
    }

    /**
     * 根据传入的浮点数获取Money对象，货币为人民币CNY
     *
     * @param yuan 单位为元，只接受两位小数
     */
    public static Money getMoneyFromYuan(Double yuan) {
        return Money.of(CNY_CURRENCY, yuan);
    }

    /**
     * 根据传入的浮点数获取Money对象，货币为人民币CNY
     *
     * @param yuan 单位为元，最好是两位小数，超过位数会根据指定的方式进行处理
     * @param roundingMode 多于小数处理方式
     */
    public static Money getMoneyFromYuan(Double yuan, RoundingMode roundingMode) {
        return Money.of(CNY_CURRENCY, BigDecimal.valueOf(yuan), roundingMode);
    }

    /**
     * 根据传入的BigDecimal获取Money对象，货币为人民币CNY
     *
     * @param yuan 单位为元，只接受两位小数
     */
    public static Money getMoneyFromYuan(BigDecimal yuan) {
        return Money.of(CNY_CURRENCY, yuan);
    }

    /**
     * 根据传入的BigDecimal获取Money对象，货币为人民币CNY
     *
     * @param yuan 单位为元，最好是两位小数，超过位数会根据指定的方式进行处理
     * @param roundingMode 多于小数处理方式
     */
    public static Money getMoneyFromYuan(BigDecimal yuan, RoundingMode roundingMode) {
        return Money.of(CNY_CURRENCY, yuan, roundingMode);
    }

    /**
     * 将Money转为Long，单位为分。
     */
    public static Long toCent(Money money) {
        if (money == null) {
            return null;
        }
        return money.getAmountMinorLong();
    }

    /**
     * 将Money转为String，单位为元
     */
    public static String toYuanString(Money money) {
        if (money == null) {
            return "";
        }
        return money.getAmount().toString();
    }

    /**
     * 将Money转换为BigDecimal，单位为元
     */
    public static BigDecimal toYuan(Money money) {
        if (money == null) {
            return null;
        }
        return money.getAmount();
    }
}
