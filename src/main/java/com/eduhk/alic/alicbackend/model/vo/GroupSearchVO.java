package com.eduhk.alic.alicbackend.model.vo;

import com.eduhk.alic.alicbackend.common.constant.GroupDemonTypeEnum;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

/**
 * @author FuSu
 * @date 2025/2/9 19:28
 */
@Data
public class GroupSearchVO{
    @Length(max = 100, message="keyword cannot longer than 100 chars")
    private String keyword;
    private GroupDemonTypeEnum groupDemonTypeEnum;
    private PageRequestVO pageRequestVO;
}
