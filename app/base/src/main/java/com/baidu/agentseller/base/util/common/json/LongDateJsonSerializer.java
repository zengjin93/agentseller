/*
 * Copyright (C) 2016 Baidu, Inc. All Rights Reserved.
 */
package com.baidu.agentseller.base.util.common.json;

import java.io.IOException;
import java.util.Date;

import org.joda.time.DateTime;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

/**
 * 针对yyyy-MM-dd HH:mm:ss的JSON序列化器
 *
 * @author dingxuefeng
 */
public class LongDateJsonSerializer extends JsonSerializer<Date> {
    private static final String PATTERN = "yyyy-MM-dd HH:mm:ss";

    @Override
    public void serialize(Date value, JsonGenerator jgen, SerializerProvider provider)
        throws IOException, JsonProcessingException {
        long time = value.getTime();
        DateTime dt = new DateTime(time);
        jgen.writeString(dt.toString(PATTERN));
    }
}
