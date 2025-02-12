package com.eduhk.alic.alicbackend.model.vo;

import com.eduhk.alic.alicbackend.common.constant.GroupDemonTypeEnum;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * @author FuSu
 * @date 2025/2/12 14:43
 */
@Data
public class GroupTagSearchVO {

    private String keyword;

    @NotNull(message = "tagId cannot be null")
    @Min(value = 1, message = "tagId must be greater than 0")
    private Long tagId;

}
