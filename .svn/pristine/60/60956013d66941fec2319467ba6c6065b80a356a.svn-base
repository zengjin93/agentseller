/*
 * Copyright (C) 2016 Baidu, Inc. All Rights Reserved.
 */
package com.baidu.agentseller.base.util.common.json;

import java.io.IOException;

import com.baidu.agentseller.base.util.common.pagination.Paginator;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

/**
 * Paginator分页对象的JSON序列化工具
 *
 * @author dingxuefeng
 */
public class PaginatorJsonSerializer extends JsonSerializer<Paginator> {

    @Override
    public void serialize(Paginator value, JsonGenerator jgen, SerializerProvider provider)
        throws IOException, JsonProcessingException {
        int page = value.getPage();
        int items = value.getItems();
        int itemsPerPage = value.getItemsPerPage();
        jgen.writeStartObject();
        jgen.writeNumberField("page", page);
        jgen.writeNumberField("items", items);
        jgen.writeNumberField("itemsPerPage", itemsPerPage);
        jgen.writeEndObject();
    }
}
