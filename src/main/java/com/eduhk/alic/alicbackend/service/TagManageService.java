package com.eduhk.alic.alicbackend.service;

import com.eduhk.alic.alicbackend.common.constant.ResultCode;
import com.eduhk.alic.alicbackend.common.exception.BaseException;
import com.eduhk.alic.alicbackend.dao.ChatTagGroupRelationMapper;
import com.eduhk.alic.alicbackend.dao.GroupInfoMapper;
import com.eduhk.alic.alicbackend.dao.TagInfoMapper;
import com.eduhk.alic.alicbackend.model.entity.ChatTagGroupRelationEntity;
import com.eduhk.alic.alicbackend.model.entity.GroupInfoEntity;
import com.eduhk.alic.alicbackend.model.entity.TagInfoEntity;
import com.eduhk.alic.alicbackend.model.vo.TagGroupInfoVO;
import com.eduhk.alic.alicbackend.model.vo.TagGroupVO;
import com.eduhk.alic.alicbackend.model.vo.TagInfoVO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;

/**
 * @author FuSu
 * @date 2025/1/21 23:23
 */
@Service
public class TagManageService {
    @Resource
    TagInfoMapper tagInfoMapper;

    @Resource
    GroupInfoMapper groupInfoMapper;

    @Resource
    ChatTagGroupRelationMapper chatTagGroupRelationMapper;

    public Long createNewTag(String tagName, Long userId) {
        TagInfoEntity tagInfoEntity = new TagInfoEntity();
        tagInfoEntity.setTagName(tagName);
        tagInfoEntity.setCreateUser(userId);
        tagInfoMapper.insert(tagInfoEntity);
        return tagInfoEntity.getTagId();
    }

    public void deleteTagById(Long tagId) {
        tagInfoMapper.updateTagCondition(tagId, tagId);
    }

    public void verifyUserAdmission(Long userId, Long tagId) {
        TagInfoEntity tagInfoEntity = tagInfoMapper.selectById(tagId);
        if (tagInfoEntity == null) {
            throw new BaseException(ResultCode.PARAMS_IS_INVALID);
        }
        if (!Objects.equals(tagInfoEntity.getCreateUser(), userId)) {
            throw new BaseException(ResultCode.UNAUTHORIZED);
        }
    }

    public void removeGroup(TagGroupInfoVO tagGroupInfoVO) {
        List<ChatTagGroupRelationEntity> chatTagGroupRelationEntities = tagGroupInfoVO.getGroupIdList().stream().map(
                groupId -> {
                    ChatTagGroupRelationEntity chatTagGroupRelationEntity = new ChatTagGroupRelationEntity();
                    chatTagGroupRelationEntity.setGroupId(groupId);
                    chatTagGroupRelationEntity.setTagId(tagGroupInfoVO.getTagId());
                    return chatTagGroupRelationEntity;
                }
        ).toList();

        chatTagGroupRelationMapper.batchHardDeleteRelations(chatTagGroupRelationEntities);
    }

    public void addGroup(TagGroupInfoVO tagGroupInfoVO) {
        List<ChatTagGroupRelationEntity> chatTagGroupRelationEntities = tagGroupInfoVO.getGroupIdList().stream().map(
                groupId -> {
                    ChatTagGroupRelationEntity chatTagGroupRelationEntity = new ChatTagGroupRelationEntity();
                    chatTagGroupRelationEntity.setGroupId(groupId);
                    chatTagGroupRelationEntity.setTagId(tagGroupInfoVO.getTagId());
                    return chatTagGroupRelationEntity;
                }
        ).toList();
        chatTagGroupRelationMapper.batchInsertRelations(chatTagGroupRelationEntities);

    }

    public List<TagInfoVO> getTagList(Long userId) {
        List<TagInfoEntity> tagInfoEntities = tagInfoMapper.selectByUser(userId);
        List<TagInfoVO> tagInfoVOS = tagInfoEntities.stream().map(tagInfoEntity -> {
            TagInfoVO tagInfoVO = new TagInfoVO();
            tagInfoVO.setTagName(tagInfoEntity.getTagName());
            tagInfoVO.setTagId(tagInfoEntity.getTagId());
            return tagInfoVO;
        }).toList();
        return tagInfoVOS;
    }

    public List<TagGroupVO> getGroupBindTagList(Long tagId) {
        List<GroupInfoEntity> groupInfoEntities = groupInfoMapper.getGroupsByTagId(tagId);
        List<TagGroupVO> tagGroupVOS = groupInfoEntities.stream().map(groupInfoEntity -> {
            TagGroupVO tagGroupVO = new TagGroupVO();
            tagGroupVO.setGroupName(groupInfoEntity.getGroupName());
            tagGroupVO.setGroupId(Long.valueOf(groupInfoEntity.getGroupId()));
            tagGroupVO.setGroupPortrait(groupInfoEntity.getGroupPortrait());
            tagGroupVO.setGroupAdmin(groupInfoEntity.getGroupAdmin());
            tagGroupVO.setGroupDescription(groupInfoEntity.getGroupDescription());
            return tagGroupVO;
        }).toList();
        return tagGroupVOS;
    }

}
