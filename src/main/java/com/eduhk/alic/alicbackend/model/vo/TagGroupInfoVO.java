package com.eduhk.alic.alicbackend.model.vo;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author FuSu
 * @date 2025/1/30 20:34
 */
@Data
public class TagGroupInfoVO {
    @NotNull(message = "tagId cannot be null")
    @Min(value = 1, message = "tagId must be greater than 0")
    private Long tagId;      // Tag name
    private String tagName;
    private List<Long> groupIdList;
}
