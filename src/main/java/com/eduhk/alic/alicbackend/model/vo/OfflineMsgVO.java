package com.eduhk.alic.alicbackend.model.vo;

import lombok.Data;

/**
 * @author FuSu
 * @date 2025/3/3 16:42
 */
@Data
public class OfflineMsgVO {
    private Long userId;
    private int pageNum;
    private int pageSize;
}
