package com.eduhk.alic.alicbackend.model.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * @author FuSu
 * @date 2025/1/13 12:56
 */
@Getter
@Setter
public class UserInfoEntity {
    private Long userId; // Primary Key
    private String userEmail; // User email
    private Integer userCondition; // Account status: 0 for inactived, 1 for active, 2 for deactivated
    private String userName; // Username
    private String userPortrait; // User profile picture
    private String password; // User password (encrypted storage)

    @TableField(value = "create_time",fill = FieldFill.INSERT)
//    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime; // Registration time

    @TableField(value = "create_time",fill =FieldFill.INSERT_UPDATE)
//    @JsonFormat(timezone = "GMT+8")
    private Date updateTime;

    private Date deleteTime; // Deactivation time
    private String salt;
}
