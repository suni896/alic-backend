package com.eduhk.alic.alicbackend.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.eduhk.alic.alicbackend.model.entity.ChatBotInfoEntity;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * @author FuSu
 * @date 2025/1/21 16:29
 */
@Mapper
public interface ChatGroupBotMapper extends BaseMapper<ChatBotInfoEntity> {

    @Insert("INSERT INTO chat_group_bot (group_id, bot_name, bot_prompt, bot_context, access_type, create_time, update_time, delete_time) " +
            "VALUES (#{groupId}, #{botName}, #{botPrompt}, #{botContext}, #{accessType}, #{createTime}, #{updateTime}, #{deleteTime})")
    @Options(useGeneratedKeys = true, keyProperty = "botId")
    int insert(ChatBotInfoEntity chatGroupBot);

    @Insert({
            "<script>",
            "INSERT INTO chat_group_bot (group_id, bot_name, bot_prompt, bot_context, access_type, create_time, update_time, delete_time) VALUES ",
            "<foreach collection='chatGroupBots' item='bot' separator=','>",
            "(#{bot.groupId}, #{bot.botName}, #{bot.botPrompt}, #{bot.botContext}, #{bot.accessType}, #{bot.createTime}, #{bot.updateTime}, #{bot.deleteTime})",
            "</foreach>",
            "</script>"
    })
    @Options(useGeneratedKeys = true, keyProperty = "botId")
    int insertBatch(@Param("chatGroupBots") List<ChatBotInfoEntity> chatGroupBots);

    @Select("SELECT * FROM chat_group_bot WHERE bot_id = #{botId}")
    ChatBotInfoEntity selectById(@Param("botId") Long botId);

    @Select("SELECT * FROM chat_group_bot WHERE group_id = #{groupId}")
    List<ChatBotInfoEntity> selectByGroupId(@Param("groupId") Long groupId);

    @Select("SELECT * FROM chat_group_bot WHERE group_id = #{groupId} and access_type = 1")
    List<ChatBotInfoEntity> selectByGroupIdMember(@Param("groupId") Long groupId);


    @Update("UPDATE chat_group_bot SET bot_name = #{botName}, bot_prompt = #{botPrompt}, bot_context = #{botContext}, " +
            "access_type = #{accessType}, update_time = #{updateTime}, delete_time = #{deleteTime} " +
            "WHERE bot_id = #{botId}")
    int update(ChatBotInfoEntity chatGroupBot);

    @Delete("DELETE FROM chat_group_bot WHERE bot_id = #{botId}")
    int deleteById(@Param("botId") Long botId);
}
