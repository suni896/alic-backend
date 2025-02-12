package com.eduhk.alic.alicbackend.model.vo;

import io.swagger.annotations.ApiParam;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

/**
 * @author FuSu
 * @date 2025/2/9 19:36
 */
@Data
public class GroupSearchInfoVO {
    private Long groupId;
    private String groupName;       // Group name
    private String groupDescription; // Group description
    private Integer groupType;      // Group type (0: private, 1: public)
    private Long adminId;
    private String adminName;
    private Integer memberCount;

}
