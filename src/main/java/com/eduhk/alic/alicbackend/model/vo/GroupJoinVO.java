package com.eduhk.alic.alicbackend.model.vo;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 * @author FuSu
 * @date 2025/2/5 11:27
 */
@Data
public class GroupJoinVO {
    @NotNull(message = "groupId cannot be null")
    @Min(value = 1, message = "groupId must be greater than 0")
    private Long groupId;

    @NotNull(message = "joinMemberID cannot be null")
    @Min(value = 1, message = "joinMemberID must be greater than 0")
    private Long joinMemberID;

    @Pattern(regexp = "^[A-Za-z0-9]{1,20}$", message = "password pattern error")
    private String password;
}
