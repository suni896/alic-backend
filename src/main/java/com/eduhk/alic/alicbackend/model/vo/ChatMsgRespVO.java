package com.eduhk.alic.alicbackend.model.vo;

import com.eduhk.alic.alicbackend.common.constant.MemberTypeEnum;
import com.eduhk.alic.alicbackend.common.constant.MessageTypeEnum;
import lombok.Data;

import java.util.Date;

/**
 * @author FuSu
 * @date 2025/2/20 16:55
 */
@Data
public class ChatMsgRespVO {
    private Long groupId;
    private Long senderId;
    private String content;
    private Integer msgType;
//    private MessageTypeEnum msgType;
    private Date createTime;
    private MemberTypeEnum senderType;

}
