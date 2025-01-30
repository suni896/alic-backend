package com.eduhk.alic.alicbackend.model.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * @author FuSu
 * @date 2025/1/21 15:48
 */
@Data
@Setter
@Getter
public class TimeEntity {

    @TableField(value = "create_time",fill = FieldFill.INSERT)
    private Date createTime;

    @TableField(value = "create_time",fill =FieldFill.INSERT_UPDATE)
    private Date updateTime;

    private Date deleteTime;
}
