package com.baidu.agentseller.service.api.exception;

public class AgentsellerException extends RuntimeException {

    private static final long serialVersionUID = 73951957376962427L;
    /** 错误枚举 */
    protected AgentsellerErrorCode error;
    /** 错误描述 */
    private String errorMsg;

    public AgentsellerException() {
        super();
    }

    public AgentsellerException(AgentsellerErrorCode error) {
        super();
        this.error = error;
    }

    /**
     * 需要展示特殊错误码描述
     * 
     * @param error
     * @param errorMsg
     */
    public AgentsellerException(AgentsellerErrorCode error, String errorMsg) {
        super();
        this.errorMsg = errorMsg;
        this.error = error;
    }

    /**
     * 获取错误码
     * 
     * @return
     */
    public String getErrorCode() {
        if (this.getError() != null) {
            return this.getError().getErrorCode();
        }
        return null;
    }

    /**
     * 对应的数字错误吗
     * 
     * @return
     */
    public String getErrorNumCode() {
        if (this.getError() != null) {
            return this.getError().getErrorNumCode();
        }
        return null;
    }

    /**
     * 获取错误描述
     * 
     * @return
     */
    public String getErrorMsg() {

        if (errorMsg != null && !errorMsg.equals("")) {
            return errorMsg;
        }
        if (this.getError() != null) {
            return this.getError().getErrorMsg();
        }
        return null;
    }

    /**
     * 获取原始错误描述
     * 
     * @return
     */
    public String getOriginalErrorMsg() {
        return errorMsg;
    }

    /**
     * @return the error
     */
    public AgentsellerErrorCode getError() {
        return error;
    }

    /**
     * @param error the error to set
     */
    public void setError(AgentsellerErrorCode error) {
        this.error = error;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    @Override
    public String toString() {
        return "error[" + error + "],errorMsg[" + errorMsg + "]";
    }

    /**
     * 出于性能考虑，去掉了同步和堆栈
     */
    // @Override
    // public Throwable fillInStackTrace() {
    // return this;
    // }

}
