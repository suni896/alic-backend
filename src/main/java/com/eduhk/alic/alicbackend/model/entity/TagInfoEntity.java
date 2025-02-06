package com.eduhk.alic.alicbackend.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.util.Date;

/**
 * @author FuSu
 * @date 2025/1/21 15:49
 */
@Data
@TableName("chat_tag")
public class TagInfoEntity {
    @TableId(value = "tag_id", type = IdType.AUTO)
    private Long tagId;          // Tag ID
    private Long createUser;     // Tag owner
    private String tagName;      // Tag name
//    private TimeEntity tagTime;
    @TableField(value = "create_time",fill = FieldFill.INSERT)
    private Date createTime;

    @TableField(value = "create_time",fill =FieldFill.INSERT_UPDATE)
    private Date updateTime;

    private Date deleteTime;
//    private String groupIdArr;  // Group ID array (comma-separated)
    private Long tagCondition;
}
