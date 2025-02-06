package com.eduhk.alic.alicbackend.model.vo;

import lombok.Data;

/**
 * @author FuSu
 * @date 2025/2/5 11:27
 */
@Data
public class GroupJoinVO {
    private Long groupId;
    private Long joinMemberID;
    private String password;
}
