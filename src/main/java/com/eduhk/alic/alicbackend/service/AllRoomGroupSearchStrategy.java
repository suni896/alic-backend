package com.eduhk.alic.alicbackend.service;

import com.eduhk.alic.alicbackend.model.vo.GroupSearchInfoVO;
import com.eduhk.alic.alicbackend.model.vo.PageRequestVO;
import com.eduhk.alic.alicbackend.model.vo.PageVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author FuSu
 * @date 2025/2/15 14:55
 */
@Service
public class AllRoomGroupSearchStrategy implements GroupSearchStrategy {

    private final GroupSearchService groupSearchService;

    @Autowired
    public AllRoomGroupSearchStrategy(GroupSearchService groupSearchService) {
        this.groupSearchService = groupSearchService;
    }

    @Override
    public PageVO<GroupSearchInfoVO> searchGroup(String keyword, PageRequestVO pageRequestVO, Long userId) {
        return groupSearchService.searchAllGroup(keyword, pageRequestVO.getPageNum(), pageRequestVO.getPageSize());
    }
}
