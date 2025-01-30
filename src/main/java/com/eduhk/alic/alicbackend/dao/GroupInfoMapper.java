package com.eduhk.alic.alicbackend.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.eduhk.alic.alicbackend.model.entity.GroupInfoEntity;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * @author FuSu
 * @date 2025/1/21 16:11
 */
@Mapper
public interface GroupInfoMapper extends BaseMapper<GroupInfoEntity> {
    @Insert("INSERT INTO chat_group (group_name, group_description, group_portrait, group_type, create_time, delete_time, update_time, group_admin, password) " +
            "VALUES (#{groupName}, #{groupDescription}, #{groupPortrait}, #{groupType}, #{createTime}, #{deleteTime}, #{updateTime}, #{groupAdmin}, #{password})")
    @Options(useGeneratedKeys = true, keyProperty = "groupId", keyColumn = "group_id")
    int insert(GroupInfoEntity chatGroup);

    @Select("SELECT * FROM chat_group WHERE group_id = #{groupId}")
    GroupInfoEntity selectById(@Param("groupId") Long groupId);

    @Select("SELECT * FROM chat_group")
    List<GroupInfoEntity> selectAll();

    @Update("UPDATE chat_group SET group_name = #{groupName}, group_description = #{groupDescription}, group_portrait = #{groupPortrait}, " +
            "group_type = #{groupType}, update_time = #{updateTime}, group_admin = #{groupAdmin}, password = #{password} " +
            "WHERE group_id = #{groupId}")
    int update(GroupInfoEntity chatGroup);

    @Delete("DELETE FROM chat_group WHERE group_id = #{groupId}")
    int deleteById(@Param("groupId") Long groupId);

    @Select("""
        SELECT cg.group_id, cg.group_name, cg.group_description, cg.group_portrait,
               cg.group_type, cg.create_time, cg.delete_time, cg.update_time, cg.group_admin
        FROM chat_group cg
        JOIN chat_tag_group_relation ctr ON cg.group_id = ctr.group_id
        WHERE ctr.tag_id = #{tagId}
    """)
    @Results({
            @Result(column = "group_id", property = "groupId"),
            @Result(column = "group_name", property = "groupName"),
            @Result(column = "group_description", property = "groupDescription"),
            @Result(column = "group_portrait", property = "groupPortrait"),
            @Result(column = "group_type", property = "groupType"),
            @Result(column = "create_time", property = "createTime"),
            @Result(column = "delete_time", property = "deleteTime"),
            @Result(column = "update_time", property = "updateTime"),
            @Result(column = "group_admin", property = "groupAdmin")
    })
    List<GroupInfoEntity> getGroupsByTagId(@Param("tagId") Long tagId);

}
