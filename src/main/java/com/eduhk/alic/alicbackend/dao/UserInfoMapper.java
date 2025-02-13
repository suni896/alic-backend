package com.eduhk.alic.alicbackend.dao;

import com.eduhk.alic.alicbackend.model.entity.GroupInfoEntity;
import com.eduhk.alic.alicbackend.model.entity.GroupMemberInfoEntity;
import com.eduhk.alic.alicbackend.model.entity.UserInfoEntity;
import org.apache.ibatis.annotations.*;

import java.time.LocalDateTime;
import java.util.List;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
/**
 * @author FuSu
 * @date 2025/1/13 12:37
 */
@Mapper
public interface UserInfoMapper extends BaseMapper<UserInfoEntity> {
    @Insert("INSERT INTO user_info (user_email, user_condition, user_name, user_portrait, password, create_time, delete_time, update_time, salt) " +
            "VALUES (#{userEmail}, #{userCondition}, #{userName}, #{userPortrait}, #{password}, #{createTime}, #{deleteTime}, #{updateTime}, #{salt})")
    @Options(useGeneratedKeys = true, keyProperty = "userId")
    void insertUser(UserInfoEntity user);

    @Override
    @Insert("INSERT INTO user_info (user_email, user_condition, user_name, user_portrait, password, create_time, delete_time, update_time, salt) " +
            "VALUES (#{userEmail}, #{userCondition}, #{userName}, #{userPortrait}, #{password}, #{createTime}, #{deleteTime}, #{updateTime}, #{salt})")
    @Options(useGeneratedKeys = true, keyProperty = "userId", keyColumn = "user_id")
    int insert(UserInfoEntity entity);

    @Select("SELECT * FROM user_info WHERE user_id = #{userId}")
    UserInfoEntity findUserById(Long userId);

    @Select("SELECT * FROM user_info WHERE user_email = #{userEmail} AND user_condition = #{userCondition}")
    UserInfoEntity findUserByEmailAndCondition(@Param("userEmail") String userEmail, @Param("userCondition") Integer userCondition);

    @Select("SELECT * FROM user_info")
    List<UserInfoEntity> findAllUsers();

    @Update("UPDATE user_info SET user_condition = #{userCondition}, delete_time = #{deleteTime} WHERE user_id = #{userId}")
    void deleteUserStatus(@Param("userId") Long userId, @Param("userCondition") Integer userCondition, @Param("deleteTime") LocalDateTime deleteTime);

    @Update("UPDATE user_info SET user_condition = #{userCondition} WHERE user_email = #{userEmail} and user_condition < 100 ")
    void updateUserStatus(@Param("userEmail") String userEmail, @Param("userCondition") Integer userCondition);

    @Update("UPDATE user_info SET password = #{password}, salt = #{salt} WHERE user_email = #{userEmail} and user_condition < 100")
    void updatePassword(@Param("password") String password, @Param("salt") String salt, @Param("userEmail") String userEmail);

    @Update("UPDATE user_info SET user_portrait = #{userPortrait} WHERE user_id = #{userId}")
    void updatePortrait(@Param("userId") Long userId, @Param("userPortrait") byte[] userPortrait);

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

    @Select("""
        SELECT ui.user_id, ui.user_email, ui.user_portrait, ui.user_name, cg.group_admin
        FROM user_info ui 
        JOIN chat_group_user_info cgui ON ui.user_id = cgui.user_id 
        JOIN chat_group cg ON cgui.group_id = cg.group_id
        WHERE cgui.group_id = #{groupId} and ui.user_condition < 100
    """)
    @Results({
            @Result(column = "user_id", property = "userId"),
            @Result(column = "user_email", property = "userEmail"),
            @Result(column = "user_portrait", property = "userPortrait"),
            @Result(column = "user_name", property = "userName"),
            @Result(column = "group_admin", property = "adminId")
    })
    List<GroupMemberInfoEntity> getUsersByGroupId(@Param("groupId") Long groupId);


    @Select("""
        SELECT COUNT(*)
        FROM user_info ui 
        JOIN chat_group_user_info cgui ON ui.user_id = cgui.user_id 
        WHERE cgui.group_id = #{groupId} and ui.user_condition < 100
    """)
    Long getUserCountByGroupId(@Param("groupId") Long groupId);
}
