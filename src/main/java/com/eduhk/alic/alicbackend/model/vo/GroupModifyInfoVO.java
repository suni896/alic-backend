package com.eduhk.alic.alicbackend.model.vo;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.List;

/**
 * @author FuSu
 * @date 2025/2/6 13:33
 */
@Data
public class GroupModifyInfoVO {
    @NotNull(message = "groupId cannot be null")
    @Min(value = 1, message = "groupId must be greater than 0")
    private Long groupId;

    @Length(max = 200, message = "groupDescription length error")
    private String groupDescription; // Group description
    private String password;        // Password for private group
    private List<ChatBotDemonVO> modifyChatBotVOS; //原来就有的bot
    private List<ChatBotVO> addChatBotVOList; //新增的bot
}
