package com.eduhk.alic.alicbackend.model.vo;

import lombok.Data;

import java.util.List;

/**
 * @author FuSu
 * @date 2025/1/30 20:34
 */
@Data
public class TagGroupInfoVO {
    private Long tagId;      // Tag name
    private String tagName;
    private List<Long> groupIdList;
}
