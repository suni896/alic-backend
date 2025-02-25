package com.eduhk.alic.alicbackend.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.eduhk.alic.alicbackend.model.entity.ChatMsgEntity;
import com.eduhk.alic.alicbackend.model.entity.GroupDetailInfoEntity;
import org.apache.ibatis.annotations.*;

import java.util.Date;
import java.util.List;

/**
 * @author FuSu
 * @date 2025/2/20 15:14
 */
public interface ChatMsgMapper extends BaseMapper<ChatMsgEntity> {

    // 插入消息
    @Insert("INSERT INTO `chat_msg` (`msg_source_type`, `msg_dest`, `msg_source`, `msg_type`, `msg_content`, `create_time`) " +
            "VALUES (#{msgSourceType}, #{msgDest}, #{msgSource}, #{msgType}, #{msgContent}, #{createTime})")
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    int insertChatMsg(ChatMsgEntity chatMsg);

    // 根据群组ID查询消息
    @Select("SELECT * FROM `chat_msg` WHERE `msg_dest` = #{msgDest} ORDER BY `create_time` DESC")
    List<ChatMsgEntity> getMessagesByGroupId(@Param("msgDest") Long msgDest);

    // 根据消息ID查询消息
    @Select("SELECT * FROM `chat_msg` WHERE `id` = #{id}")
    ChatMsgEntity getMessageById(@Param("id") Long id);

    // 查询指定时间段内的消息
    @Select("SELECT * FROM `chat_msg` WHERE `create_time` BETWEEN #{startTime} AND #{endTime} ORDER BY `create_time` DESC")
    List<ChatMsgEntity> getMessagesByTimeRange(@Param("startTime") String startTime, @Param("endTime") String endTime);

    @Select("SELECT * FROM `chat_msg` WHERE `msg_dest` = #{groupID} AND `create_time` > #{retriveTime} ORDER BY `create_time` ASC")
    List<ChatMsgEntity> getOfflineMessagesForUser(@Param("groupID") Long groupID, @Param("retriveTime")Date retriveTime);

    @Select("SELECT * FROM `chat_msg` WHERE `msg_dest` = #{groupID} orderBy `id` DESC")
    Page<ChatMsgEntity> getChatMsgByPage(@Param("groupID") Long groupID, IPage<ChatMsgEntity> page);

    @Select("SELECT * FROM chat_msg WHERE `msg_dest` = #{groupID} AND id > #{id} orderBy `id` ASC")
    List<ChatMsgEntity> getMessagesByIdGreaterThan(@Param("groupID") Long groupID, @Param("id") Long id);
}
