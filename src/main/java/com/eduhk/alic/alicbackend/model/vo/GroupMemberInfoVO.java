package com.eduhk.alic.alicbackend.model.vo;

import com.eduhk.alic.alicbackend.common.constant.GroupMemberType;
import lombok.Data;

/**
 * @author FuSu
 * @date 2025/2/13 10:52
 */
@Data
public class GroupMemberInfoVO {
    private Long userId; // Primary Key
    private String userEmail; // User email
    private String userName; // Username
    private String userPortrait; // User profile picture
    private GroupMemberType groupMemberType;
}
