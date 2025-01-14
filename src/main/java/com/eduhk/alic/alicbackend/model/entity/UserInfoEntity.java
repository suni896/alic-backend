package com.eduhk.alic.alicbackend.model.entity;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * @author FuSu
 * @date 2025/1/13 12:56
 */
@Getter
@Setter
public class UserInfoEntity {
    private Long userId; // Primary Key
    private String userEmail; // User email
    private Integer userCondition; // Account status: 0 for inactived, 1 for active, 2 for deactivated
    private String userName; // Username
    private String userPortrait; // User profile picture
    private String password; // User password (encrypted storage)
    private LocalDateTime createTime; // Registration time
    private LocalDateTime deleteTime; // Deactivation time
    private String salt;
}
