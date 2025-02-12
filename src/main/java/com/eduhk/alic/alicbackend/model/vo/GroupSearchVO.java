package com.eduhk.alic.alicbackend.model.vo;

import com.eduhk.alic.alicbackend.common.constant.GroupDemonTypeEnum;
import lombok.Data;

/**
 * @author FuSu
 * @date 2025/2/9 19:28
 */
@Data
public class GroupSearchVO{

    private String keyword;
    private GroupDemonTypeEnum groupDemonTypeEnum;
    private PageRequestVO pageRequestVO;
}
