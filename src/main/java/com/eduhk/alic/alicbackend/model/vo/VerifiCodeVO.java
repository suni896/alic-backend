package com.eduhk.alic.alicbackend.model.vo;

import com.eduhk.alic.alicbackend.common.constant.SmtpTypeEnum;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 * @author FuSu
 * @date 2025/1/15 22:31
 */
@Getter
@Setter
public class VerifiCodeVO {

    @NotNull(message = "verifiCode cannot be null")
    @Pattern(regexp = "^\\d{6}$", message = "verifiCode pattern error")
    private String verifiCode;

    @Email(message = "email pattern error")
    private String email;

    @NotNull(message = "type cannot be null")
    private SmtpTypeEnum type;
}
