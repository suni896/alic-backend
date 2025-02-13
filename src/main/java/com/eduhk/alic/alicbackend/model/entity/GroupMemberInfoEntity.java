package com.eduhk.alic.alicbackend.model.entity;

import lombok.Data;

/**
 * @author FuSu
 * @date 2025/2/13 00:03
 */
@Data
public class GroupMemberInfoEntity {
    private Long userId; // Primary Key
    private String userEmail; // User email
    private String userName; // Username
    private byte[] userPortrait; // User profile picture
    private Long adminId;
}
