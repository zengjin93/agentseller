package com.baidu.agentseller.dal.entity;

public class RefundInfoWithBLOBs extends RefundInfo {
    private String reqResponse;

    private String notifyReq;

    public String getReqResponse() {
        return reqResponse;
    }

    public void setReqResponse(String reqResponse) {
        this.reqResponse = reqResponse == null ? null : reqResponse.trim();
    }

    public String getNotifyReq() {
        return notifyReq;
    }

    public void setNotifyReq(String notifyReq) {
        this.notifyReq = notifyReq == null ? null : notifyReq.trim();
    }
}