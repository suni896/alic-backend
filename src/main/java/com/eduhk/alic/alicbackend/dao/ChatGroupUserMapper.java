package com.eduhk.alic.alicbackend.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.eduhk.alic.alicbackend.model.entity.ChatGroupUserEntity;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * @author FuSu
 * @date 2025/2/4 14:35
 */
@Mapper
public interface ChatGroupUserMapper extends BaseMapper<ChatGroupUserEntity> {

    // 插入数据
    @Insert("INSERT INTO chat_group_user_info (group_id, user_id, create_time, update_time) " +
            "VALUES (#{groupId}, #{userId},  #{createTime}, #{updateTime})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(ChatGroupUserEntity chatGroupUserInfo);

    // 根据 ID 物理删除（彻底删除）
    @Delete("DELETE FROM chat_group_user_info WHERE id = #{id}")
    int deletePermanentlyById(@Param("id") Long id);

    @Delete("DELETE FROM chat_group_user_info WHERE user_id = #{userId} AND group_id = #{groupId}")
    int deleteUserGroupRelation(@Param("userId") Long userId, @Param("groupId") Long groupId);

    // 根据 groupId 查询所有成员
    @Select("SELECT * FROM chat_group_user_info WHERE group_id = #{groupId}")
    List<ChatGroupUserEntity> selectByGroupId(@Param("groupId") Long groupId);

    // 根据 userId 查询该用户所在的所有群组
    @Select("SELECT * FROM chat_group_user_info WHERE user_id = #{userId}")
    List<ChatGroupUserEntity> selectByUserId(@Param("userId") Long userId);

    //查询user是否在组里
    @Select("SELECT * FROM chat_group_user_info WHERE user_id = #{userId} AND group_id = #{groupId}")
    ChatGroupUserEntity selectByUserIdAndGroupId(@Param("groupId") Long groupId, @Param("userId") Long userId);

    // 更新用户所在的群组信息
    @Update("UPDATE chat_group_user_info SET group_id = #{groupId}, update_time = NOW() WHERE id = #{id}")
    int updateGroup(@Param("id") Long id, @Param("groupId") Long groupId);

}
