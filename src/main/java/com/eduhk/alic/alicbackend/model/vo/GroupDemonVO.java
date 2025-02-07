package com.eduhk.alic.alicbackend.model.vo;

import io.swagger.annotations.ApiParam;
import lombok.Data;
import lombok.NonNull;

import java.util.List;

/**
 * @author FuSu
 * @date 2025/1/22 18:09
 */
@Data
public class GroupDemonVO {
    private Long groupId;
    private String groupName;       // Group name
    private String groupDescription; // Group description
    private Integer groupType;      // Group type (0: private, 1: public)
    private String password;        // Password for private group
    private String portrait;        // Password for private group
    private List<ChatBotDemonVO> chatBots;

}
