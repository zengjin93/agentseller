package com.baidu.agentseller.service.api.model;

import java.io.Serializable;

import com.baidu.agentseller.service.api.exception.AgentsellerErrorCode;

public class GenericBaseResponse implements Serializable {

    /**
    * 
    */
    private static final long serialVersionUID = -8976930227507726116L;
    /**
     * 结果码
     */
    private String resultCode = "SUCCESS";
    /**
     * 结果描述
     */
    private String resultMsg = "";

    /**
     * 默认构造
     */
    public GenericBaseResponse() {
    }

    public String getResultCode() {
        return resultCode;
    }

    public void setResultCode(String resultCode) {
        this.resultCode = resultCode;
    }

    public String getResultMsg() {
        return resultMsg;
    }

    public void setResultMsg(String resultMsg) {
        this.resultMsg = resultMsg;
    }

    /**
     * 带错误码构造
     * 
     * @param error
     */
    public GenericBaseResponse(AgentsellerErrorCode error) {
        this.setResultCode(error.getErrorCode());
        this.setResultMsg(error.getErrorMsg());
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "BaseResponse [resultCode=" + resultCode + ", resultMsg=" + resultMsg + "]";
    }

}
