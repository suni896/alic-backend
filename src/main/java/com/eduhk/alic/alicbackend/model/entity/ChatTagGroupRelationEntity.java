package com.eduhk.alic.alicbackend.model.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.util.Date;

/**
 * @author FuSu
 * @date 2025/1/30 22:02
 */
@Data
public class ChatTagGroupRelationEntity {
    private Long id;
    private Long tagId;
    private Long groupId;
//    private TimeEntity timeEntity;
    @TableField(value = "create_time",fill = FieldFill.INSERT)
    private Date createTime;

    @TableField(value = "create_time",fill =FieldFill.INSERT_UPDATE)
    private Date updateTime;

    private Date deleteTime;
}
