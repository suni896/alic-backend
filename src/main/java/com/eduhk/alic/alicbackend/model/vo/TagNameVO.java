package com.eduhk.alic.alicbackend.model.vo;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 * @author FuSu
 * @date 2025/1/31 09:52
 */
@Data
public class TagNameVO {

    @NotNull(message = "tagName cannot be null")
    @Pattern(regexp = "^[A-Za-z0-9]{1,20}$", message = "tagName pattern error")
    private String tagName;
}
