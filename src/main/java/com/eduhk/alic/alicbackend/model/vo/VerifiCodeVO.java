package com.eduhk.alic.alicbackend.model.vo;

import com.eduhk.alic.alicbackend.common.constant.SmtpTypeEnum;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

/**
 * @author FuSu
 * @date 2025/1/15 22:31
 */
@Getter
@Setter
public class VerifiCodeVO {

    @NonNull
    private String verifiCode;

    @NonNull
    private SmtpTypeEnum type;
}
