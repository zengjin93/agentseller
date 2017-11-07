package com.baidu.agentseller.service.api.model.enums;

import org.apache.commons.lang3.StringUtils;

public enum CusOrderStatus {

    UNPAID("0", "待支付"),

    CANCEL("1", "已取消"),

    PAID("2", "已支付"),
    
    CONFIRM("3", "已确认"),

    DELIVERED("4", "已发货"),

    ACCOMPLISH("5", "已完成"),

    REFUND("6", "退款申请中"),

    REFUNDING("7", "退款中"),

    REFUNDED("8", "已退款"),

    DELETED("9", "已删除"),

    ;

    private String code;
    private String desc;

    private CusOrderStatus(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static boolean equals(CusOrderStatus status, String code) {
        if (StringUtils.isBlank(code)) {
            return false;
        }
        CusOrderStatus codeEnum = getByCode(code);
        return status == codeEnum;
    }

    public static CusOrderStatus getByCode(String code) {
        if (StringUtils.isBlank(code)) {
            return null;
        }
        for (CusOrderStatus status : values()) {
            if (StringUtils.equals(status.getCode(), code)) {
                return status;
            }
        }
        return null;
    }
    

    /**
     * @return the code
     */
    public String getCode() {
        return code;
    }

    /**
     * @param code the code to set
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * @return the desc
     */
    public String getDesc() {
        return desc;
    }

    /**
     * @param desc the desc to set
     */
    public void setDesc(String desc) {
        this.desc = desc;
    }

}
