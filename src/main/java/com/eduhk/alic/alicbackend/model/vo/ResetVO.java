package com.eduhk.alic.alicbackend.model.vo;

import com.eduhk.alic.alicbackend.common.constant.SmtpTypeEnum;
import io.swagger.annotations.ApiParam;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 * @author FuSu
 * @date 2025/1/17 11:49
 */
@Getter
@Setter
public class ResetVO {

    @NotNull(message = "userEmail cannot be null")
    @Email(message = "userEmail pattern error")
    private String userEmail; // User email

    @Pattern(regexp = "^[A-Za-z0-9!@#$%^&*()_+\\-={}\\$begin:math:display$\\$end:math:display$:;\"'<>,.?/~`|\\\\]{6,20}$", message = "password pattern error")
    private String newPassword;

    private String token;

    @NotNull(message = "type cannot be null")
    @ApiParam(value = "1: register, 3: reset", example = "1")
    private SmtpTypeEnum type;
}
