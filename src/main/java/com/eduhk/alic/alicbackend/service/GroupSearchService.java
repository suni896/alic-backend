package com.eduhk.alic.alicbackend.service;

import com.eduhk.alic.alicbackend.dao.GroupInfoMapper;
import com.eduhk.alic.alicbackend.model.entity.GroupDetailInfoEntity;
import com.eduhk.alic.alicbackend.model.entity.GroupTagEntity;
import com.eduhk.alic.alicbackend.model.vo.*;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
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

    public PageVO<GroupSearchInfoVO> searchAllGroup(String keyword, Integer pageNum, Integer pageSize) {
        Page<Object> page = PageHelper.startPage(pageNum, pageSize);
        //todo groupid搜索
        List<GroupDetailInfoEntity> groupInfoEntities = groupInfoMapper.getAllGroups(keyword);
        List<GroupSearchInfoVO> groupSearchInfoVOS = groupInfoEntities.stream().map(
                groupInfoEntity -> {
                    GroupSearchInfoVO groupSearchInfoVO = new GroupSearchInfoVO();
                    groupSearchInfoVO.setGroupId(groupInfoEntity.getGroupId());
                    groupSearchInfoVO.setGroupDescription(groupInfoEntity.getGroupDescription());
                    groupSearchInfoVO.setGroupName(groupInfoEntity.getGroupName());
                    groupSearchInfoVO.setGroupType(groupInfoEntity.getGroupType());
                    groupSearchInfoVO.setAdminId(groupInfoEntity.getGroupAdmin());
                    //todo 头像
                    groupSearchInfoVO.setAdminName(groupInfoEntity.getGroupAdminName());
                    groupSearchInfoVO.setMemberCount(groupInfoEntity.getGroupMemberCount());
                    return groupSearchInfoVO;
                }
        ).toList();

        PageVO<GroupSearchInfoVO> pageGroupSearchInfoVO = new PageVO<> (pageSize, pageNum, (int) page.getTotal(), groupSearchInfoVOS);
        return pageGroupSearchInfoVO;
    }

    public PageVO<GroupSearchInfoVO> searchPublicGroup(String keyword, Integer pageNum, Integer pageSize) {
        //todo groupid搜索

        Page<Object> page = PageHelper.startPage(pageNum, pageSize);
        //todo groupid搜索
        List<GroupDetailInfoEntity> groupInfoEntities = groupInfoMapper.getPublicGroups(keyword);
        List<GroupSearchInfoVO> groupSearchInfoVOS = groupInfoEntities.stream().map(
                groupInfoEntity -> {
                    GroupSearchInfoVO groupSearchInfoVO = new GroupSearchInfoVO();
                    groupSearchInfoVO.setGroupId(groupInfoEntity.getGroupId());
                    groupSearchInfoVO.setGroupDescription(groupInfoEntity.getGroupDescription());
                    groupSearchInfoVO.setGroupName(groupInfoEntity.getGroupName());
                    groupSearchInfoVO.setGroupType(groupInfoEntity.getGroupType());
                    groupSearchInfoVO.setAdminId(groupInfoEntity.getGroupAdmin());
                    //todo 头像
                    groupSearchInfoVO.setAdminName(groupInfoEntity.getGroupAdminName());
                    groupSearchInfoVO.setMemberCount(groupInfoEntity.getGroupMemberCount());
                    return groupSearchInfoVO;
                }
        ).toList();

        PageVO<GroupSearchInfoVO> pageGroupSearchInfoVO = new PageVO<> (pageSize, pageNum, (int) page.getTotal(), groupSearchInfoVOS);
        return pageGroupSearchInfoVO;
    }

    public PageVO<GroupSearchInfoVO> searchJoinGroup(Long userId, String keyword, Integer pageNum, Integer pageSize) {
        Page<Object> page = PageHelper.startPage(pageNum, pageSize);
        //todo groupid搜索
        List<GroupDetailInfoEntity> groupInfoEntities = groupInfoMapper.getJoinGroups(userId, keyword);
        List<GroupSearchInfoVO> groupSearchInfoVOS = groupInfoEntities.stream().map(
                groupInfoEntity -> {
                    GroupSearchInfoVO groupSearchInfoVO = new GroupSearchInfoVO();
                    groupSearchInfoVO.setGroupId(groupInfoEntity.getGroupId());
                    groupSearchInfoVO.setGroupDescription(groupInfoEntity.getGroupDescription());
                    groupSearchInfoVO.setGroupName(groupInfoEntity.getGroupName());
                    groupSearchInfoVO.setGroupType(groupInfoEntity.getGroupType());
                    groupSearchInfoVO.setAdminId(groupInfoEntity.getGroupAdmin());
                    //todo 头像
                    groupSearchInfoVO.setAdminName(groupInfoEntity.getGroupAdminName());
                    groupSearchInfoVO.setMemberCount(groupInfoEntity.getGroupMemberCount());
                    return groupSearchInfoVO;
                }
        ).toList();

        PageVO<GroupSearchInfoVO> pageGroupSearchInfoVO = new PageVO<> (pageSize, pageNum, (int) page.getTotal(), groupSearchInfoVOS);
        return pageGroupSearchInfoVO;
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
