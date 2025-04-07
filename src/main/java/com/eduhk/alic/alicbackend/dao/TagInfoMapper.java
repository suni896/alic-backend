package com.eduhk.alic.alicbackend.dao;

import cn.hutool.db.sql.Order;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.eduhk.alic.alicbackend.model.entity.TagInfoEntity;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * @author FuSu
 * @date 2025/1/21 16:21
 */
@Mapper
public interface TagInfoMapper extends BaseMapper<TagInfoEntity> {

    @Insert("INSERT INTO chat_tag (create_user, tag_name, create_time, update_time, delete_time, tag_condition) " +
            "VALUES (#{createUser}, #{tagName}, #{createTime}, #{updateTime}, #{deleteTime}, 1)")
    @Options(useGeneratedKeys = true, keyProperty = "tagId", keyColumn = "tag_id")
    int insert(TagInfoEntity chatTag);

    @Select("SELECT * FROM chat_tag WHERE tag_id = #{tagId}")
    TagInfoEntity selectById(@Param("tagId") Long tagId);

    @Select("SELECT * FROM chat_tag WHERE create_user = #{createUser} and tag_condition = 1")
    List<TagInfoEntity> selectByUser(@Param("createUser") Long createUser);

    @Select({
            "<script>",
            "SELECT * FROM chat_tag",
            "WHERE 1=1 ",
            "AND create_user = #{createUser} AND tag_condition = 1",
            "<if test='keyword != null and keyword != \"\"'>",
            "    AND (tag_name LIKE CONCAT('%', #{keyword}, '%') )",
            "</if>",
            "</script>"
    })
    Page<TagInfoEntity> selectByUserSearchByTagName(@Param("createUser") Long createUser, @Param("keyword") String keyword, IPage<TagInfoEntity> page);

    @Update("UPDATE chat_tag SET tag_condition = #{tagCondition} WHERE tag_id = #{tagId}")
    int updateTagCondition(@Param("tagId") Long tagId, @Param("tagCondition") Long tagCondition);

    @Delete("DELETE FROM chat_tag WHERE tag_id = #{tagId}")
    int deleteById(@Param("tagId") Long tagId);

    @Select("""
        SELECT t.*
        FROM chat_tag t
        JOIN chat_tag_group_relation r ON t.tag_id = r.tag_id
        WHERE r.group_id = #{groupId}
          AND t.tag_condition = 1
    """)
    List<TagInfoEntity> getTagsByGroupId(@Param("groupId") Long groupId);

}
