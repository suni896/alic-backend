package com.eduhk.alic.alicbackend.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.eduhk.alic.alicbackend.model.entity.ChatTagGroupRelationEntity;
import org.apache.ibatis.annotations.*;

import java.util.Date;
import java.util.List;

/**
 * @author FuSu
 * @date 2025/1/30 22:03
 */
@Mapper
public interface ChatTagGroupRelationMapper extends BaseMapper<ChatTagGroupRelationEntity> {
    @Insert("""
        INSERT INTO chat_tag_group_relation (tag_id, group_id, create_time, delete_time)
        VALUES (#{tagId}, #{groupId}, #{createTime}, NULL)
    """)
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insertRelation(@Param("tagId") Long tagId, @Param("groupId") Long groupId, @Param("createTime") Date createTime);

    /**
     * 根据 tag_id 查询关联的 group_id 列表
     */
    @Select("""
        SELECT group_id FROM chat_tag_group_relation WHERE tag_id = #{tagId} AND delete_time IS NULL
    """)
    List<Long> getGroupIdsByTagId(@Param("tagId") Long tagId);
    /**
     * 彻底删除某个 tag 和 group 的关系
     */
    @Delete("""
        DELETE FROM chat_tag_group_relation WHERE tag_id = #{tagId} AND group_id = #{groupId}
    """)
    int hardDeleteRelation(@Param("tagId") Long tagId, @Param("groupId") Long groupId);

    /**
     * 批量插入 tag-group 关系
     */
    @Insert("""
        <script>
        INSERT INTO chat_tag_group_relation (tag_id, group_id, create_time, delete_time)
        VALUES 
        <foreach collection="relations" item="relation" separator=",">
            (#{relation.tagId}, #{relation.groupId}, #{relation.createTime}, NULL)
        </foreach>
        </script>
    """)
    int batchInsertRelations(@Param("relations") List<ChatTagGroupRelationEntity> relations);

    /**
     * 批量硬删除 tag-group 关系
     */
    @Delete("""
        <script>
        DELETE FROM chat_tag_group_relation
        WHERE (tag_id, group_id) IN
        <foreach collection="relations" item="relation" open="(" separator="),(" close=")">
            #{relation.tagId}, #{relation.groupId}
        </foreach>
        </script>
    """)
    int batchHardDeleteRelations(@Param("relations") List<ChatTagGroupRelationEntity> relations);
}
