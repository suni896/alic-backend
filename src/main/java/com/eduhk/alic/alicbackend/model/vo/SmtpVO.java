package com.eduhk.alic.alicbackend.model.vo;

import com.eduhk.alic.alicbackend.common.constant.SmtpTypeEnum;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

/**
 * @author FuSu
 * @date 2025/1/13 15:00
 */
@Getter
@Setter
public class SmtpVO {
    @NonNull
    private String userEmail; // User email


    private String userName;


    private String password;

    @NonNull
    private SmtpTypeEnum type;
}
