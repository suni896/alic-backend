package com.eduhk.alic.alicbackend.model.vo;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 * @author FuSu
 * @date 2025/1/16 11:46
 */
@Getter
@Setter
public class LogInVO {
    @NotNull(message = "email cannot be null")
    @Email(message = "email pattern error")
    private String userEmail; // User email

    @Pattern(regexp = "^[A-Za-z0-9!@#$%^&*()_+\\-={}\\$begin:math:display$\\$end:math:display$:;\"'<>,.?/~`|\\\\]{6,20}$", message = "password pattern error")
    private String password;


}
