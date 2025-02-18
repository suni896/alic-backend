package com.eduhk.alic.alicbackend.model.vo;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * @author FuSu
 * @date 2025/2/15 13:57
 */
@Data
public class TagSearchInfoVO {

    private PageRequestVO pageRequestVO;

    @NotNull(message = "tagId cannot be null")
    @Min(value = 1, message = "tagId must be greater than 0")
    private Long tagId;
}
