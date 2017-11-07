/*
 * Copyright (C) 2016 Baidu, Inc. All Rights Reserved.
 */
package com.baidu.agentseller.base.util.common.json;

import java.io.IOException;
import java.util.Date;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

/**
 * 针对yyyy-MM-dd HH:mm:ss的JSON反序列化器
 *
 * @author dingxuefeng
 */
public class LongDateJsonDeserializer extends JsonDeserializer<Date> {
    private static final String PATTERN = "yyyy-MM-dd HH:mm:ss";

    @Override
    public Date deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        String date = jp.getText();
        DateTime dt = DateTime.parse(date, DateTimeFormat.forPattern(PATTERN));
        return dt.toDate();
    }
}
