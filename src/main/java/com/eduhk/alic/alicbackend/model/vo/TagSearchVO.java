package com.eduhk.alic.alicbackend.model.vo;

import com.eduhk.alic.alicbackend.common.constant.GroupDemonTypeEnum;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

/**
 * @author FuSu
 * @date 2025/2/15 10:14
 */
@Data
public class TagSearchVO {
    @Length(max = 100, message="keyword cannot longer than 100 chars")
    private String keyword;
    private PageRequestVO pageRequestVO;
}
