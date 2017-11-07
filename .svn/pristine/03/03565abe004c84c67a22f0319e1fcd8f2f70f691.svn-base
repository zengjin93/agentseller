package com.baidu.agentseller.service.api.model.enums;

import org.apache.commons.lang3.StringUtils;

public enum BusOrderStatus {

    UNPAID("0", "待支付"),

    PAID("1", "已支付"),
    
    CONFIRM("2", "已确认"),

    POSTAGE("3", "邮费设定成功"),

    POSTAGE_PAY("4", "邮费付款成功"),

    ;

    private String code;
    private String desc;

    private BusOrderStatus(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static boolean equals(BusOrderStatus status, String code) {
        if (StringUtils.isBlank(code)) {
            return false;
        }
        BusOrderStatus codeEnum = getByCode(code);
        return status == codeEnum;
    }

    public static BusOrderStatus getByCode(String code) {
        if (StringUtils.isBlank(code)) {
            return null;
        }
        for (BusOrderStatus status : values()) {
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
