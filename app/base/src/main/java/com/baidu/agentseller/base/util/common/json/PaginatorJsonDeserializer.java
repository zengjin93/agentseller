/*
 * Copyright (C) 2016 Baidu, Inc. All Rights Reserved.
 */
package com.baidu.agentseller.base.util.common.json;

import java.io.IOException;

import com.baidu.agentseller.base.util.common.pagination.Paginator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

/**
 * Paginator分页对象的JSON反序列化工具
 *
 * @author dingxuefeng
 */
public class PaginatorJsonDeserializer extends JsonDeserializer<Paginator> {
    @Override
    public Paginator deserialize(JsonParser jp, DeserializationContext ctxt)
        throws IOException, JsonProcessingException {
        JsonNode node = jp.getCodec().readTree(jp);
        int page = node.get("page").asInt();
        int items = node.get("items").asInt();
        int itemsPerPage = node.get("itemsPerPage").asInt();

        Paginator paginator = new Paginator(itemsPerPage, items);
        paginator.setPage(page);
        return paginator;
    }
}
