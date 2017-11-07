/**
 * Copyright 2013-2015 Baifubao.com
 */
package com.baidu.agentseller.service.api.model;

import com.baidu.agentseller.service.api.exception.AgentsellerErrorCode;

public class GenericResponse<T> extends GenericBaseResponse {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 2154183617915043132L;

    private T content;

    /**
     * you know
     * 
     * @param errorCode
     * @param content
     */
    public GenericResponse(AgentsellerErrorCode errorCode, T content) {
        super(errorCode);
        this.content = content;
    }

    public GenericResponse() {
    }

    public GenericResponse(AgentsellerErrorCode errorCode) {
        super(errorCode);
    }

    public GenericResponse(T content) {
        this(AgentsellerErrorCode.SUCCESS, content);
    }

    /**
     * @return the content
     */
    public T getContent() {
        return content;
    }

    /**
     * @param content the content to set
     */
    public void setContent(T content) {
        this.content = content;
    }

}
