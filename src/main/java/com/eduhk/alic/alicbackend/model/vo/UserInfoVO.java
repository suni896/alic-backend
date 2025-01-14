package com.eduhk.alic.alicbackend.model.vo;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * @author FuSu
 * @date 2025/1/13 13:13
 */
@Getter
@Setter
@Data
public class UserInfoVO {
    private Long userId; // Primary Key
    private String userEmail; // User email
    private String userName; // Username
    private String userPortrait; // User profile picture
}
