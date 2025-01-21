package com.eduhk.alic.alicbackend.model.vo;

import com.eduhk.alic.alicbackend.common.constant.SmtpTypeEnum;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

/**
 * @author FuSu
 * @date 2025/1/17 11:49
 */
@Getter
@Setter
public class ResetVO {

    @NonNull
    private String userEmail; // User email

    private String newPassword;

    @NonNull
    private SmtpTypeEnum type;
}
