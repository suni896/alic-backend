package com.eduhk.alic.alicbackend.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * @author FuSu
 * @date 2025/1/21 15:47
 */
@Data
@Setter
@Getter
@TableName("chat_group")
public class GroupInfoEntity {
    @TableId(value = "group_id", type = IdType.AUTO)
    private Long groupId;           // Group ID
    private String groupName;       // Group name
    private String groupDescription; // Group description
    private String groupPortrait;   // Group portrait
    private Integer groupType;      // Group type (0: private, 1: public)
//    private TimeEntity timeEntity;
    @TableField(value = "create_time",fill = FieldFill.INSERT)
    private Date createTime;

    @TableField(value = "create_time",fill =FieldFill.INSERT_UPDATE)
    private Date updateTime;

    private Date deleteTime;
    private Long groupAdmin;        // Group admin ID
    private String password;        // Password for private group
    private String salt;
}
