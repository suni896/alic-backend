package com.eduhk.alic.alicbackend.model.vo;

import io.swagger.annotations.ApiParam;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 * @author FuSu
 * @date 2025/1/23 14:18
 */
@Data
public class ChatBotDemonVO {
    @NotNull(message = "botId cannot be null")
    private Long botId;
    @NotNull(message = "botName cannot be null")
    @Pattern(regexp = "^[A-Za-z0-9]{1,20}$", message = "botName pattern error")
    private String botName;         // Bot name
    @NotNull(message = "botPrompt cannot be null")
    @Length(max = 400, message="botPrompt cannot longer than 400 chars")
    private String botPrompt;       // Bot prompt
    @Min(value = 1, message = "botContext must be greater than 0")
    @Max(value = 20, message = "botContext must be less than 0")
    private Integer botContext;     // Bot context
    @ApiParam(value = "0: bot could access by admin only, 1: bot could access by all group members")
    private Integer accessType;     // Access type (0: admin only, 1: all group members)
}
