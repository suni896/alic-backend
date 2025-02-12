package com.eduhk.alic.alicbackend.service;

import cn.hutool.crypto.SecureUtil;
import com.eduhk.alic.alicbackend.common.constant.GroupMemberType;
import com.eduhk.alic.alicbackend.common.constant.ResultCode;
import com.eduhk.alic.alicbackend.common.exception.BaseException;
import com.eduhk.alic.alicbackend.dao.ChatGroupUserMapper;
import com.eduhk.alic.alicbackend.dao.GroupInfoMapper;
import com.eduhk.alic.alicbackend.dao.UserInfoMapper;
import com.eduhk.alic.alicbackend.model.entity.ChatGroupUserEntity;
import com.eduhk.alic.alicbackend.model.entity.GroupInfoEntity;
import com.eduhk.alic.alicbackend.model.entity.UserInfoEntity;
import com.eduhk.alic.alicbackend.model.vo.GroupJoinVO;
import com.eduhk.alic.alicbackend.model.vo.GroupRemoveVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * @author FuSu
 * @date 2025/2/4 14:59
 */
@Service
public class GroupUserService {

    @Resource
    ChatGroupUserMapper chatGroupUserMapper;
    @Resource
    private GroupInfoMapper groupInfoMapper;
    @Autowired
    private UserInfoMapper userInfoMapper;

    public void addUserToGroup(Long groupId, Long userId) {
        ChatGroupUserEntity info = new ChatGroupUserEntity();
        info.setGroupId(groupId);
        info.setUserId(userId);
        chatGroupUserMapper.insert(info);
    }

    public List<ChatGroupUserEntity> getUsersByGroupId(Long groupId) {
        return chatGroupUserMapper.selectByGroupId(groupId);
    }
//    public void verifyGroupExist(Long groupId) {
//        // 验证群组是否存在
//        GroupInfoEntity groupInfoEntity = groupInfoMapper.selectById(groupId);
//        if (groupInfoEntity == null) {
//            throw new BaseException(ResultCode.GROUP_NOT_EXIST);
//        }
//    }
    public Boolean verifyUserExistInGroup(Long groupId, Long userId) {
        ChatGroupUserEntity chatGroupUserEntity = chatGroupUserMapper.selectByUserIdAndGroupId(groupId, userId);
        return chatGroupUserEntity != null;
    }

    private void verifyUserExist(Long userId) {
        // 验证用户是否存在
        UserInfoEntity userInfoEntity = userInfoMapper.selectById(userId);
        if (userInfoEntity == null) {
            throw new BaseException(ResultCode.USER_NOT_EXIST);
        }
    }
    public void verifyGroupAuth(GroupJoinVO groupJoinVO, Long operatorId) {
        // 验证群组是否存在
        GroupInfoEntity groupInfoEntity = groupInfoMapper.selectById(groupJoinVO.getGroupId());
        if (groupInfoEntity == null) {
            throw new BaseException(ResultCode.GROUP_NOT_EXIST);
        }
        // 验证用户是否存在
        verifyUserExist(groupJoinVO.getJoinMemberID());
        //验证是谁发起的
        if (!Objects.equals(operatorId, groupJoinVO.getJoinMemberID()) && !Objects.equals(operatorId, groupInfoEntity.getGroupAdmin())) {
            throw new BaseException(ResultCode.NO_AUTH);

        }
        // 验证群组类型
        if (groupInfoEntity.getGroupType() == 0) {
            // 私有群组
            // 验证密码
            String salt = groupInfoEntity.getSalt();
            String md5Pwd = SecureUtil.md5(groupJoinVO.getPassword()+salt);
            System.out.println("md5Pwd: "+md5Pwd);
            System.out.println("md5Pwd: "+groupInfoEntity.getPassword());
            // 校验密码
            if (!groupInfoEntity.getPassword().equals(md5Pwd)){
                throw new BaseException(ResultCode.PASSWORD_ERROR);
            }
        }
    }

    public void verifyGroupAuth(GroupRemoveVO groupRemoveVO, Long operatorId) {
        // 验证群组是否存在
        GroupInfoEntity groupInfoEntity = groupInfoMapper.selectById(groupRemoveVO.getGroupId());
        if (groupInfoEntity == null) {
            throw new BaseException(ResultCode.GROUP_NOT_EXIST);
        }
        // 验证用户是否存在
        verifyUserExist(groupRemoveVO.getRemoveMemberId());
        //操作人是退群的人，或者操作人是admin
        if (!Objects.equals(operatorId, groupRemoveVO.getRemoveMemberId()) && !Objects.equals(operatorId, groupInfoEntity.getGroupAdmin())) {
            throw new BaseException(ResultCode.NO_AUTH);
        }
    }

    public GroupMemberType getGroupMemberType(Long groupId, Long userId){
        // 验证群组是否存在
        GroupInfoEntity groupInfoEntity = groupInfoMapper.selectById(groupId);
        if (groupInfoEntity == null) {
            throw new BaseException(ResultCode.GROUP_NOT_EXIST);
        }
        if (Objects.equals(userId, groupInfoEntity.getGroupAdmin())) {
            return GroupMemberType.ADMIN;
        } else {
            return GroupMemberType.MEMBER;
        }
    }
    public void addGroupMember(Long groupId, Long userId) {
        ChatGroupUserEntity info = new ChatGroupUserEntity();
        info.setGroupId(groupId);
        info.setUserId(userId);
        chatGroupUserMapper.insert(info);
    }

    public void removeGroupMember(Long groupId, Long userId) {
        chatGroupUserMapper.deleteUserGroupRelation(userId, groupId);
    }

}
