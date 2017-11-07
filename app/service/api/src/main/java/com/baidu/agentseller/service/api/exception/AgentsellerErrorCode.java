package com.baidu.agentseller.service.api.exception;

public enum AgentsellerErrorCode {

    // ================================== 公共错误码 ====================================================//

    SUCCESS("SUCCESS", "6000000", "处理成功"),

    FAIL("FAIL", "6000001", "失败"),

    NO_LOGIN("NO_LOGIN", "600002", "未登录"),

    PERMISSION_DENIED("PERMISSION_DENIED", "600003", "无权限操作"),

    CHECK_SIGN_ERROR("CHECK_SIGN_ERROR", "600004", "签名验证失败"),

    CHECK_STATUS_ERROR("CHECK_STATUS_ERROR", "600005", "订单状态不正确"),

    CHECK_AMOUNT_ERROR("CHECK_AMOUNT_ERROR", "600006", "订单金额不正确"),

    CHECK_FREIGHT_ERROR("CHECK_FREIGHT_ERROR", "600007", "邮费订单已存在"),

    REFUND_ERROR("REFUND_ERROR", "600008", "提交微信退款单失败"),

    PARAMS_ERROR("PARAMS_ERROR", "600008", "参数有误"),

    SYSTEM_ERROR("SYSTEM_ERROR", "609999", "系统异常"),

    ;

    private String errorCode;

    private String errorNumCode;

    private String errorDesc;

    private AgentsellerErrorCode(String errorCode, String errorNumCode, String errorDesc) {
        this.errorCode = errorCode;
        this.errorNumCode = errorNumCode;
        this.errorDesc = errorDesc;
    }

    /**
     * @return
     */
    public String getErrorCode() {
        return errorCode;
    }

    /**
     * @return
     */
    public String getErrorMsg() {
        return errorDesc;
    }

    /**
     * @return
     */
    public String getErrorNumCode() {
        return errorNumCode;
    }

    /**
     * 通过枚举<code>code</code>获得枚举
     *
     * @param code
     *
     * @return
     */
    public static AgentsellerErrorCode getByCode(String code) {
        if (code == null) {
            return null;
        }
        for (AgentsellerErrorCode value : values()) {
            if (value.getErrorCode().equals(code)) {
                return value;
            }
        }
        return null;
    }

    /**
     * 比较
     *
     * @param ebbpErrorCode
     * @param code
     *
     * @return
     */
    public static boolean equals(AgentsellerErrorCode ebbpErrorCode, String code) {
        if (code == null || code.equals("")) {
            return false;
        }

        AgentsellerErrorCode codeEnum = getByCode(code);

        return ebbpErrorCode == codeEnum;
    }

    @Override
    public String toString() {
        return "errorCode[" + getErrorCode() + "],errorNumCode[" + getErrorNumCode() + "],errorMsg[" + getErrorMsg()
                + "]";
    }

}
