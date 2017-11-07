/**
 * Copyright 2013-2013 baidu.com
 */
package com.baidu.agentseller.base.util.log.digest;

/**
 * Web摘要日志记录时，需要保存几个时间点
 * 
 * @author dingxuefeng
 */
public class WebDigestLogTimer {
    /**
     * 开始处理时间
     */
    private Long beginTime;
    /**
     * 处理结束时间
     */
    private Long processEndTime;
    /**
     * 渲染结束时间
     */
    private Long renderEndTime;

    public Long getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(Long beginTime) {
        this.beginTime = beginTime;
    }

    public Long getProcessEndTime() {
        return processEndTime;
    }

    public void setProcessEndTime(Long processEndTime) {
        this.processEndTime = processEndTime;
    }

    public Long getRenderEndTime() {
        return renderEndTime;
    }

    public void setRenderEndTime(Long renderEndTime) {
        this.renderEndTime = renderEndTime;
    }
}
