package com.eduhk.alic.alicbackend.common.constant;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

/**
 * @author FuSu
 * @date 2025/1/13 15:18
 */
@Getter
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
     * 重置密码
     */
    RESET("3", "chat:code:forget:", 5),
    ;
    @EnumValue
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
