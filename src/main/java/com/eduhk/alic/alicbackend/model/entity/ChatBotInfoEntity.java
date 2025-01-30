package com.eduhk.alic.alicbackend.model.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.util.Date;

/**
 * @author FuSu
 * @date 2025/1/21 15:49
 */
@Data
public class ChatBotInfoEntity {
    private Long botId;             // Bot ID
    private Long groupId;           // Group ID
    private String botName;         // Bot name
    private String botPrompt;       // Bot prompt
    private Integer botContext;     // Bot context
    private Integer accessType;     // Access type (0: admin only, 1: all group members)
//    private TimeEntity accessTime;
@TableField(value = "create_time",fill = FieldFill.INSERT)
private Date createTime;

    @TableField(value = "create_time",fill =FieldFill.INSERT_UPDATE)
    private Date updateTime;

    private Date deleteTime;
}
