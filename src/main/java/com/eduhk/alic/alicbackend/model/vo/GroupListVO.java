package com.eduhk.alic.alicbackend.model.vo;

import com.eduhk.alic.alicbackend.common.constant.GroupDemonTypeEnum;
import lombok.Data;

/**
 * @author FuSu
 * @date 2025/2/4 11:34
 */
@Data
public class GroupListVO {

    private GroupDemonTypeEnum groupDemonTypeEnum;

    private String searchKey;
}
