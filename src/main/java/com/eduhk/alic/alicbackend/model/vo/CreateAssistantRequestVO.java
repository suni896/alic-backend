package com.eduhk.alic.alicbackend.model.vo;

import lombok.Data;

/**
 * @author FuSu
 * @date 2025/3/5 14:26
 */
@Data
public class CreateAssistantRequestVO {
    private String instructions;
    private String model;
}
