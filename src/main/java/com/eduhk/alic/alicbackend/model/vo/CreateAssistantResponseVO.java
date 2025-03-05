package com.eduhk.alic.alicbackend.model.vo;

import lombok.Data;

import java.util.Map;

/**
 * @author FuSu
 * @date 2025/3/5 14:29
 */
@Data
public class CreateAssistantResponseVO {
    private String id;
    private String object;

    private Long createdAt;

    private String name;
    private String description;
    private String model;
    private String instructions;
    private String[] tools;

    private Double topP;

    private Double temperature;

    private Map<String, Object> toolResources;

    private Map<String, Object> metadata;

    private String responseFormat;
}
