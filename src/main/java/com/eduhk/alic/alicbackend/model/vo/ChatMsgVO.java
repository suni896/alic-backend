package com.eduhk.alic.alicbackend.model.vo;

import com.eduhk.alic.alicbackend.common.constant.MessageTypeEnum;
import lombok.Data;

import java.util.Date;

/**
 * @author FuSu
 * @date 2025/2/18 13:32
 */
@Data
public class ChatMsgVO {
    private Long groupId;
    private Long senderId;
    private String content;
    private Integer msgType;
    private MessageTypeEnum type;
    private Date createTime;
}
