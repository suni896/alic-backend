package com.eduhk.alic.alicbackend.service;

import com.eduhk.alic.alicbackend.model.vo.GroupSearchInfoVO;
import com.eduhk.alic.alicbackend.model.vo.PageRequestVO;
import com.eduhk.alic.alicbackend.model.vo.PageVO;

/**
 * @author FuSu
 * @date 2025/2/15 14:51
 */
public interface GroupSearchStrategy {
    PageVO<GroupSearchInfoVO> searchGroup(String keyword, PageRequestVO pageRequestVO, Long userId);
}
