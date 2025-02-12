package com.eduhk.alic.alicbackend.model.vo;

import lombok.Data;

/**
 * @author FuSu
 * @date 2025/2/12 15:53
 */
@Data
public class TagGroupBindVO {
    private Long groupId;           // Group ID
    private String groupName;       // Group name
    private Boolean isBinded;

    public TagGroupBindVO() {}
    public TagGroupBindVO(Long groupId, String groupName, Boolean isBinded) {
        this.groupId = groupId;
        this.groupName = groupName;
        this.isBinded = isBinded;
    }
}
