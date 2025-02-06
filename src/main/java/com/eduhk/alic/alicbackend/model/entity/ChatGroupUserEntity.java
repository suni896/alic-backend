package com.eduhk.alic.alicbackend.model.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.util.Date;

/**
 * @author FuSu
 * @date 2025/2/4 14:34
 */
@Data
public class ChatGroupUserEntity {
    private Long id;

    private Long groupId;

    private Long userId;

    @TableField(value = "create_time",fill = FieldFill.INSERT)
    private Date createTime;

    @TableField(value = "create_time",fill =FieldFill.INSERT_UPDATE)
    private Date updateTime;

    private Date deleteTime;
}
