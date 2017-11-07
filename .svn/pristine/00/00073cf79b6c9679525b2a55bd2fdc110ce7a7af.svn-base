/*
 * Copyright (C) 2016 Baidu, Inc. All Rights Reserved.
 */
package com.baidu.agentseller.base.util.common.json;

import java.io.IOException;

import org.joda.money.Money;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

/**
 * 将Money对象按最小单位序列化为JSON，例如，CNY按分输出
 *
 * @author dingxuefeng
 */
public class MoneyJsonSerializer extends JsonSerializer<Money> {

    /**
     * 序列化
     */
    @Override
    public void serialize(Money value, JsonGenerator jgen, SerializerProvider provider)
        throws IOException, JsonProcessingException {
        jgen.writeString(Long.toString(value.getAmountMinorLong()));
    }
}
