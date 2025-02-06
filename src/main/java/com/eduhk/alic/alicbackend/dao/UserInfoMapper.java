package com.eduhk.alic.alicbackend.dao;

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
}
