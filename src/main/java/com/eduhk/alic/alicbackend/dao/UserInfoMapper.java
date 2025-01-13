package com.eduhk.alic.alicbackend.dao;

import com.eduhk.alic.alicbackend.model.entity.UserInfoEntity;
import org.apache.ibatis.annotations.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author FuSu
 * @date 2025/1/13 12:37
 */
@Mapper
public interface UserInfoMapper {
    @Insert("INSERT INTO users (user_email, condition, user_name, user_portrait, password, create_time, delete_time, salt) " +
            "VALUES (${userEmail}, ${condition}, ${userName}, ${userPortrait}, ${password}, ${createTime}, ${deleteTime}, ${salt})")
    @Options(useGeneratedKeys = true, keyProperty = "userId")
    void insertUser(UserInfoEntity user);

    @Select("SELECT * FROM users WHERE user_id = ${userId}")
    UserInfoEntity findUserById(Long userId);

    @Select("SELECT * FROM users WHERE user_email = ${userEmail} AND condition = ${condition}")
    UserInfoEntity findUserByEmailAndCondition(@Param("userEmail") String userEmail, @Param("condition") Integer condition);

    @Select("SELECT * FROM users")
    List<UserInfoEntity> findAllUsers();

    @Update("UPDATE users SET condition = ${condition}, delete_time = ${deleteTime} WHERE user_id = ${userId}")
    void updateUserStatus(@Param("userId") Long userId, @Param("condition") Integer condition, @Param("deleteTime") LocalDateTime deleteTime);

    @Delete("DELETE FROM users WHERE user_id = ${userId}")
    void deleteUserById(Long userId);
}
