package com.eduhk.alic.alicbackend.model.entity;

import lombok.Data;

/**
 * @author FuSu
 * @date 2025/1/22 12:37
 */
@Data
public class PasswordEntity {
    private String password;
    private String salt;
}
