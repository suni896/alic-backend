package com.eduhk.alic.alicbackend.model.vo;

import lombok.Data;

/**
 * @author FuSu
 * @date 2025/1/30 20:12
 */
@Data
public class TagGroupVO {
    private Long groupId;           // Group ID
    private String groupName;       // Group name
    private String groupDescription; // Group description
    private String groupPortrait;   // Group portrait
    private Long groupAdmin;

}
