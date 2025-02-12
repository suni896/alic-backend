package com.eduhk.alic.alicbackend.model.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.util.Date;

/**
 * @author FuSu
 * @date 2025/2/12 12:49
 */
@Data
public class GroupDetailInfoEntity {
    private Long groupId;           // Group ID
    private String groupName;       // Group name
    private String groupDescription; // Group description
    private String groupPortrait;   // Group portrait
    private Integer groupType;      // Group type (0: private, 1: public)
    private Long groupAdmin;        // Group admin ID
    private String groupAdminName;
    private Integer groupMemberCount;
}
