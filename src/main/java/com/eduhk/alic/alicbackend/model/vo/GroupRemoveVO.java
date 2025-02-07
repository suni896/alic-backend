package com.eduhk.alic.alicbackend.model.vo;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * @author FuSu
 * @date 2025/2/5 15:19
 */
@Data
public class GroupRemoveVO {

    @NotNull(message = "groupId cannot be null")
    @Min(value = 1, message = "groupId must be greater than 0")
    private Long groupId;

    @NotNull(message = "removeMemberId cannot be null")
    @Min(value = 1, message = "removeMemberId must be greater than 0")
    private Long removeMemberId;
}
