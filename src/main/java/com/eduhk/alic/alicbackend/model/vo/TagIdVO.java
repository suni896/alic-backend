package com.eduhk.alic.alicbackend.model.vo;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * @author FuSu
 * @date 2025/1/31 13:44
 */
@Data
public class TagIdVO {
    @NotNull(message = "tagId cannot be null")
    @Min(value = 1, message = "tagId must be greater than 0")
    private Long tagId;
}
