package com.eduhk.alic.alicbackend.model.vo;

import lombok.Data;

/**
 * @author FuSu
 * @date 2025/1/28 15:21
 */
@Data
public class GroupTagDemonVO {
    private String groupName;       // Group name
    private Long groupId;
    private String groupDescription; // Group description
    private Integer groupType;      // Group type (0: private, 1: public)
    private String portrait;        // Password for private group
    private String createUser;
}
