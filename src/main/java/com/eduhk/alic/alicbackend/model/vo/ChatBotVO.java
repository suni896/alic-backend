package com.eduhk.alic.alicbackend.model.vo;

import io.swagger.annotations.ApiParam;
import lombok.Data;

/**
 * @author FuSu
 * @date 2025/1/21 16:51
 */
@Data
public class ChatBotVO {
    private String botName;         // Bot name
    private String botPrompt;       // Bot prompt
    private Integer botContext;     // Bot context
    @ApiParam(value = "0: bot could access by admin only, 1: bot could access by all group members")
    private Integer accessType;     // Access type (0: admin only, 1: all group members)
}
