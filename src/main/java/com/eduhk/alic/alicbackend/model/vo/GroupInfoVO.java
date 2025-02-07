package com.eduhk.alic.alicbackend.model.vo;

import io.swagger.annotations.ApiParam;
import lombok.Data;
import lombok.NonNull;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.List;

/**
 * @author FuSu
 * @date 2025/1/21 16:51
 */
@Data
public class GroupInfoVO {
    @NotNull(message = "groupName cannot be null")
    @Pattern(regexp = "^[A-Za-z0-9]{1,20}$", message = "groupName pattern error")
    private String groupName;       // Group name

    @Length(max = 200, message = "groupDescription length error")
    private String groupDescription; // Group description

    @NotNull(message = "groupType cannot be null")
    @ApiParam(value = "0: private, 1: public", example = "0")
    private Integer groupType;      // Group type (0: private, 1: public)

    @Pattern(regexp = "^[A-Za-z0-9]{1,20}$", message = "password pattern error")
    private String password;        // Password for private group
    private List<ChatBotVO> chatBotVOList;

    public GroupInfoVO() {}
}
