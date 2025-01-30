package com.eduhk.alic.alicbackend.model.vo;

import io.swagger.annotations.ApiParam;
import lombok.Data;
import lombok.NonNull;

import java.util.List;

/**
 * @author FuSu
 * @date 2025/1/21 16:51
 */
@Data
public class GroupInfoVO {
    @NonNull
    private String groupName;       // Group name
    private String groupDescription; // Group description
    @NonNull
    @ApiParam(value = "0: private, 1: public", example = "0")
    private Integer groupType;      // Group type (0: private, 1: public)
    private String password;        // Password for private group
    private List<ChatBotVO> chatBotVOList;

    public GroupInfoVO() {}
}
