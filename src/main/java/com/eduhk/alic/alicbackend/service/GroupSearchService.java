package com.eduhk.alic.alicbackend.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.eduhk.alic.alicbackend.dao.GroupInfoMapper;
import com.eduhk.alic.alicbackend.model.entity.GroupDetailInfoEntity;
import com.eduhk.alic.alicbackend.model.entity.GroupTagEntity;
import com.eduhk.alic.alicbackend.model.entity.TagInfoEntity;
import com.eduhk.alic.alicbackend.model.vo.*;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author FuSu
 * @date 2025/2/9 19:32
 */
@Service
public class GroupSearchService {
    @Resource
    GroupInfoMapper groupInfoMapper;

//    public PageVO<GroupSearchInfoVO> searchAllGroup(String keyword, Integer pageNum, Integer pageSize) {
//        IPage<GroupDetailInfoEntity> page = new Page<>(pageNum, pageSize);
//        //todo groupid搜索
//        Page<GroupDetailInfoEntity> groupInfoEntities = groupInfoMapper.getAllGroups(keyword, page);
//        List<GroupSearchInfoVO> groupSearchInfoVOS = groupInfoEntities.getRecords().stream().map(
//                groupInfoEntity -> {
//                    GroupSearchInfoVO groupSearchInfoVO = new GroupSearchInfoVO();
//                    groupSearchInfoVO.setGroupId(groupInfoEntity.getGroupId());
//                    groupSearchInfoVO.setGroupDescription(groupInfoEntity.getGroupDescription());
//                    groupSearchInfoVO.setGroupName(groupInfoEntity.getGroupName());
//                    groupSearchInfoVO.setGroupType(groupInfoEntity.getGroupType());
//                    groupSearchInfoVO.setAdminId(groupInfoEntity.getGroupAdmin());
//                    //todo 头像
//                    groupSearchInfoVO.setAdminName(groupInfoEntity.getGroupAdminName());
//                    groupSearchInfoVO.setMemberCount(groupInfoEntity.getGroupMemberCount());
//                    return groupSearchInfoVO;
//                }
//        ).toList();
//
//        PageVO<GroupSearchInfoVO> pageGroupSearchInfoVO = new PageVO<> (pageSize, pageNum, groupInfoEntities.getTotal() , groupSearchInfoVOS);
//        return pageGroupSearchInfoVO;
//    }
//
//    public PageVO<GroupSearchInfoVO> searchPublicGroup(String keyword, Integer pageNum, Integer pageSize) {
////        todo groupid搜索
//        IPage<GroupDetailInfoEntity> page = new Page<>(pageNum, pageSize);
//        //todo groupid搜索
//        Page<GroupDetailInfoEntity> groupInfoEntities = groupInfoMapper.getPublicGroups(keyword, page);
//        List<GroupSearchInfoVO> groupSearchInfoVOS = groupInfoEntities.getRecords().stream().map(
//                groupInfoEntity -> {
//                    GroupSearchInfoVO groupSearchInfoVO = new GroupSearchInfoVO();
//                    groupSearchInfoVO.setGroupId(groupInfoEntity.getGroupId());
//                    groupSearchInfoVO.setGroupDescription(groupInfoEntity.getGroupDescription());
//                    groupSearchInfoVO.setGroupName(groupInfoEntity.getGroupName());
//                    groupSearchInfoVO.setGroupType(groupInfoEntity.getGroupType());
//                    groupSearchInfoVO.setAdminId(groupInfoEntity.getGroupAdmin());
//                    //todo 头像
//                    groupSearchInfoVO.setAdminName(groupInfoEntity.getGroupAdminName());
//                    groupSearchInfoVO.setMemberCount(groupInfoEntity.getGroupMemberCount());
//                    return groupSearchInfoVO;
//                }
//        ).toList();
//
//        PageVO<GroupSearchInfoVO> pageGroupSearchInfoVO = new PageVO<> (pageSize, pageNum, groupInfoEntities.getTotal(), groupSearchInfoVOS);
//        return pageGroupSearchInfoVO;
//    }
//
//    public PageVO<GroupSearchInfoVO> searchJoinGroup(Long userId, String keyword, Integer pageNum, Integer pageSize) {
//        IPage<GroupDetailInfoEntity> page = new Page<>(pageNum, pageSize);
//        //todo groupid搜索
//        Page<GroupDetailInfoEntity> groupInfoEntities = groupInfoMapper.getJoinGroups(userId, keyword, page);
//        List<GroupSearchInfoVO> groupSearchInfoVOS = groupInfoEntities.getRecords().stream().map(
//                groupInfoEntity -> {
//                    GroupSearchInfoVO groupSearchInfoVO = new GroupSearchInfoVO();
//                    groupSearchInfoVO.setGroupId(groupInfoEntity.getGroupId());
//                    groupSearchInfoVO.setGroupDescription(groupInfoEntity.getGroupDescription());
//                    groupSearchInfoVO.setGroupName(groupInfoEntity.getGroupName());
//                    groupSearchInfoVO.setGroupType(groupInfoEntity.getGroupType());
//                    groupSearchInfoVO.setAdminId(groupInfoEntity.getGroupAdmin());
//                    //todo 头像
//                    groupSearchInfoVO.setAdminName(groupInfoEntity.getGroupAdminName());
//                    groupSearchInfoVO.setMemberCount(groupInfoEntity.getGroupMemberCount());
//                    return groupSearchInfoVO;
//                }
//        ).toList();
//
//        PageVO<GroupSearchInfoVO> pageGroupSearchInfoVO = new PageVO<> (pageSize, pageNum, groupInfoEntities.getTotal(), groupSearchInfoVOS);
//        return pageGroupSearchInfoVO;
//    }

    private List<GroupSearchInfoVO> convertToGroupSearchInfoVOList(List<GroupDetailInfoEntity> groupInfoEntities) {
        return groupInfoEntities.stream().map(groupInfoEntity -> {
            GroupSearchInfoVO groupSearchInfoVO = new GroupSearchInfoVO();
            groupSearchInfoVO.setGroupId(groupInfoEntity.getGroupId());
            groupSearchInfoVO.setGroupDescription(groupInfoEntity.getGroupDescription());
            groupSearchInfoVO.setGroupName(groupInfoEntity.getGroupName());
            groupSearchInfoVO.setGroupType(groupInfoEntity.getGroupType());
            groupSearchInfoVO.setAdminId(groupInfoEntity.getGroupAdmin());
            // TODO: 头像
            groupSearchInfoVO.setAdminName(groupInfoEntity.getGroupAdminName());
            groupSearchInfoVO.setMemberCount(groupInfoEntity.getGroupMemberCount());
            return groupSearchInfoVO;
        }).collect(Collectors.toList());
    }

    private PageVO<GroupSearchInfoVO> createPageVO(IPage<GroupDetailInfoEntity> groupInfoEntities, List<GroupSearchInfoVO> groupSearchInfoVOS, Integer pageNum, Integer pageSize) {
        return new PageVO<>(pageSize, pageNum, groupInfoEntities.getTotal(), groupSearchInfoVOS);
    }

    public PageVO<GroupSearchInfoVO> searchAllGroup(String keyword, Integer pageNum, Integer pageSize) {
        IPage<GroupDetailInfoEntity> page = new Page<>(pageNum, pageSize);
        // TODO: groupid搜索
        Page<GroupDetailInfoEntity> groupInfoEntities = groupInfoMapper.getAllGroups(keyword, page);
        List<GroupSearchInfoVO> groupSearchInfoVOS = convertToGroupSearchInfoVOList(groupInfoEntities.getRecords());
        return createPageVO(groupInfoEntities, groupSearchInfoVOS, pageNum, pageSize);
    }

    public PageVO<GroupSearchInfoVO> searchPublicGroup(String keyword, Integer pageNum, Integer pageSize) {
        IPage<GroupDetailInfoEntity> page = new Page<>(pageNum, pageSize);
        // TODO: groupid搜索
        Page<GroupDetailInfoEntity> groupInfoEntities = groupInfoMapper.getPublicGroups(keyword, page);
        List<GroupSearchInfoVO> groupSearchInfoVOS = convertToGroupSearchInfoVOList(groupInfoEntities.getRecords());
        return createPageVO(groupInfoEntities, groupSearchInfoVOS, pageNum, pageSize);
    }

    public PageVO<GroupSearchInfoVO> searchJoinGroup(Long userId, String keyword, Integer pageNum, Integer pageSize) {
        IPage<GroupDetailInfoEntity> page = new Page<>(pageNum, pageSize);
        // TODO: groupid搜索
        Page<GroupDetailInfoEntity> groupInfoEntities = groupInfoMapper.getJoinGroups(userId, keyword, page);
        List<GroupSearchInfoVO> groupSearchInfoVOS = convertToGroupSearchInfoVOList(groupInfoEntities.getRecords());
        return createPageVO(groupInfoEntities, groupSearchInfoVOS, pageNum, pageSize);
    }

    public List<TagGroupBindVO> searchGroupForTag(Long userId, Long tagId, String keyword) {
        List<GroupTagEntity> joinedGroup = groupInfoMapper.getJoinGroupsForTag(userId, keyword);
        if (joinedGroup.isEmpty()) {
            return Collections.emptyList();
        }
        List<GroupTagEntity> groupBindedTag = groupInfoMapper.getGroupsBindedTag(tagId, keyword);
        if (groupBindedTag.isEmpty()) {
            return joinedGroup.stream()
                    .map(group -> new TagGroupBindVO(group.getGroupId(), group.getGroupName(), false))
                    .collect(Collectors.toList());
        }

        // 取已绑定的群组IDs
        Set<Long> bindedGroupIds = groupBindedTag.stream()
                .map(GroupTagEntity::getGroupId)
                .collect(Collectors.toSet());

        List<TagGroupBindVO> result = new ArrayList<>();

        // 处理已绑定的群组
        groupBindedTag.stream()
                .map(group -> new TagGroupBindVO(group.getGroupId(), group.getGroupName(), true))
                .forEach(result::add);

        // 处理未绑定的群组
        joinedGroup.stream()
                .filter(group -> !bindedGroupIds.contains(group.getGroupId()))
                .map(group -> new TagGroupBindVO(group.getGroupId(), group.getGroupName(), false))
                .forEach(result::add);

        return result;
    }
}
