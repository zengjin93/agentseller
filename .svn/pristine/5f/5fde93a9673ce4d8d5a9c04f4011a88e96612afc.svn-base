/*
 * Copyright (C) 2016 Baidu, Inc. All Rights Reserved.
 */
package com.baidu.agentseller.base.util.common.json;

import java.io.IOException;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.joda.money.Money;

import com.baidu.agentseller.base.util.common.MoneyUtil;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

/**
 * 将JSON字符串反序列化到Money对象，可以支持两种格式：
 * <p/>
 * 1. 以分为单位的数字，默认使用CNY
 * 2. CNY 12.3单位为元，可以指定币种
 *
 * @author dingxuefeng
 */
public class MoneyJsonDeserializer extends JsonDeserializer<Money> {
    /**
     * 反序列化
     * 如果字符串中包含非数字，但又不能被解析则会抛出异常IllegalArgumentException
     */
    @Override
    public Money deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        String text = jp.getText();
        Money money = null;
        if (StringUtils.isNotBlank(text)) {
            if (NumberUtils.isDigits(text)) {
                money = MoneyUtil.getMoneyFromCent(text);
            } else {
                money = Money.parse(text);
            }
        }
        return money;
    }
}
