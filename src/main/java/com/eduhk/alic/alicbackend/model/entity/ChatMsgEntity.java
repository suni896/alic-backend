package com.eduhk.alic.alicbackend.model.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.util.Date;

/**
 * @author FuSu
 * @date 2025/2/20 15:16
 */
@Data
public class ChatMsgEntity {

    private Long id;
    private Integer msgSourceType; // 消息来源类型 0:user 1:bot 2:system
    private Long msgDest; // 群组ID
    private Long msgSource; // 用户ID
    private Integer msgType; // 消息类型 0: txt 1: picture 2: document
    private String msgContent; // 消息内容，可能是文本或链接

    private Date createTime; // 消息创建时间

}
