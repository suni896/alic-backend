package com.eduhk.alic.alicbackend.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.eduhk.alic.alicbackend.model.entity.TagInfoEntity;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * @author FuSu
 * @date 2025/1/21 16:21
 */
@Mapper
public interface TagInfoMapper extends BaseMapper<TagInfoEntity> {

    @Insert("INSERT INTO chat_tag (create_user, tag_name, create_time, update_time, delete_time, group_id_arr, tag_condition) " +
            "VALUES (#{createUser}, #{tagName}, #{createTime}, #{updateTime}, #{deleteTime}, #{groupIdArr}), 1")
    @Options(useGeneratedKeys = true, keyProperty = "tagId")
    int insert(TagInfoEntity chatTag);

    @Select("SELECT * FROM chat_tag WHERE tag_id = #{tagId}")
    TagInfoEntity selectById(@Param("tagId") Long tagId);

    @Select("SELECT * FROM chat_tag WHERE create_user = #{createUser} and tag_condition = 1")
    List<TagInfoEntity> selectByUser(@Param("createUser") Long createUser);

    @Update("UPDATE chat_tag SET tag_name = #{tagName}, update_time = #{updateTime}, delete_time = #{deleteTime}, group_id_arr = #{groupIdArr} " +
            "WHERE tag_id = #{tagId}")
    int update(TagInfoEntity chatTag);

    @Update("UPDATE chat_tag SET tag_condition = #{tagCondition} WHERE tag_id = #{tagId}")
    int updateTagCondition(@Param("tagId") Long tagId, @Param("tagCondition") Long tagCondition);

    @Delete("DELETE FROM chat_tag WHERE tag_id = #{tagId}")
    int deleteById(@Param("tagId") Long tagId);

}
