package com.eduhk.alic.alicbackend.constant;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * @author FuSu
 * @date 2025/1/13 15:18
 */
public enum SmtpTypeEnum {
    /**
     * 注册
     */
    REGISTERED("1", "chat:code:reg:", 5),
    /**
     * 登录
     */
    LOGIN("2", "chat:code:login:", 5),
    /**
     * 忘记密码
     */
    FORGET("3", "chat:code:forget:", 5),
    ;

    @JsonValue
    private String code;
    private String prefix;
    private Integer timeout;

    SmtpTypeEnum(String code, String prefix, Integer timeout) {
        this.code = code;
        this.prefix = prefix;
        this.timeout = timeout;
    }

}
