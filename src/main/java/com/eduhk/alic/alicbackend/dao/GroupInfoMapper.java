package com.eduhk.alic.alicbackend.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.eduhk.alic.alicbackend.model.entity.GroupDetailInfoEntity;
import com.eduhk.alic.alicbackend.model.entity.GroupInfoEntity;
import com.eduhk.alic.alicbackend.model.entity.GroupTagEntity;
import com.eduhk.alic.alicbackend.model.entity.TagInfoEntity;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * @author FuSu
 * @date 2025/1/21 16:11
 */
@Mapper
public interface GroupInfoMapper extends BaseMapper<GroupInfoEntity> {
    @Insert("INSERT INTO chat_group (group_name, group_description, group_portrait, group_type, create_time, delete_time, update_time, group_admin, password, salt) " +
            "VALUES (#{groupName}, #{groupDescription}, #{groupPortrait}, #{groupType}, #{createTime}, #{deleteTime}, #{updateTime}, #{groupAdmin}, #{password}, #{salt})")
    @Options(useGeneratedKeys = true, keyProperty = "groupId", keyColumn = "group_id")
    int insert(GroupInfoEntity chatGroup);

    @Select("SELECT * FROM chat_group WHERE group_id = #{groupId}")
    GroupInfoEntity selectById(@Param("groupId") Long groupId);

    @Select("SELECT * FROM chat_group")
    List<GroupInfoEntity> selectAll();

    @Update("UPDATE chat_group SET group_description = #{groupDescription}, " +
            "update_time = #{updateTime}, password = #{password} " +
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
    Page<GroupInfoEntity> getGroupsByTagId(@Param("tagId") Long tagId, IPage<GroupInfoEntity> page);

    @Select({
            "<script>",
            "SELECT g.group_id, g.group_name, g.group_description, g.group_portrait, ",
            "       g.group_type, g.create_time, g.update_time, g.group_admin, ",
            "       u.user_name AS group_admin_name, ",
            "       COUNT(cgui.user_id) AS group_member_count ",
            "FROM chat_group g ",
            "LEFT JOIN user_info u ON g.group_admin = u.user_id ",
            "LEFT JOIN chat_group_user_info cgui ON g.group_id = cgui.group_id ",
            "WHERE 1=1 ",
            "<if test='keyword != null and keyword != \"\"'>",
            "    AND (g.group_name LIKE CONCAT('%', #{keyword}, '%') ",
            "         OR g.group_description LIKE CONCAT('%', #{keyword}, '%') ",
            "         <if test='isNumeric'>",  // 只有当 isNumeric 为 true 时，才添加 group_id 精准匹配
            "         OR g.group_id = #{keyword} ",
            "         </if>",
            "    )",
            "</if>",
            "GROUP BY g.group_id, u.user_name",
            "ORDER BY g.create_time DESC",
            "</script>"
    })
    Page<GroupDetailInfoEntity> getAllGroups(@Param("keyword") String keyword,
                                             @Param("isNumeric") boolean isNumeric,
                                             IPage<GroupDetailInfoEntity> page);


//    @Select({
//            "<script>",
//            "SELECT group_id, group_name, group_description, group_portrait, group_type, create_time, update_time, group_admin ",
//            "FROM chat_group ",
//            "WHERE 1=1 ",
//            "<if test='keyword != null and keyword != \"\"'>",
//            "    AND (group_name LIKE CONCAT('%', #{keyword}, '%') ",
//            "         OR group_description LIKE CONCAT('%', #{keyword}, '%')) ",
//            "</if>",
//            "GROUP BY group_id",
//            "</script>"
//    })
//    List<GroupInfoEntity> getAllGroups(@Param("keyword") String keyword);

//
//    @Select({
//            "<script>",
//            "SELECT group_id, group_name, group_description, group_portrait, group_type, ",
//            "       create_time, update_time, group_admin ",
//            "FROM chat_group ",
//            "WHERE 1=1 AND group_type = 1",
//            "<if test='keyword != null and keyword != \"\"'>",
//            "    AND (group_name LIKE CONCAT('%', #{keyword}, '%') ",
//            "         OR group_description LIKE CONCAT('%', #{keyword}, '%')) ",
//            "</if>",
//            "GROUP BY group_id",
//            "</script>"
//    })
//    List<GroupInfoEntity> getPublicGroups(@Param("keyword") String keyword);
@Select({
        "<script>",
        "SELECT g.group_id, g.group_name, g.group_description, g.group_portrait, ",
        "       g.group_type, g.create_time, g.update_time, g.group_admin, ",
        "       u.user_name AS group_admin_name, ",
        "       COUNT(cgui.user_id) AS group_member_count ",
        "FROM chat_group g ",
        "LEFT JOIN user_info u ON g.group_admin = u.user_id ",
        "LEFT JOIN chat_group_user_info cgui ON g.group_id = cgui.group_id ",
        "WHERE 1=1 AND g.group_type = 1",
        "<if test='keyword != null and keyword != \"\"'>",
        "    AND (g.group_name LIKE CONCAT('%', #{keyword}, '%') ",
        "         OR g.group_description LIKE CONCAT('%', #{keyword}, '%') ",
        "         <if test='isNumeric'>",  // 仅当 isNumeric 为 true 时匹配 group_id
        "         OR g.group_id = #{keyword} ",
        "         </if>",
        "    )",
        "</if>",
        "GROUP BY g.group_id, u.user_name",
        "ORDER BY g.create_time DESC",
        "</script>"
})
Page<GroupDetailInfoEntity> getPublicGroups(@Param("keyword") String keyword,
                                            @Param("isNumeric") boolean isNumeric,
                                            IPage<GroupDetailInfoEntity> page);


//    @Select({
//            "<script>",
//            "SELECT g.group_id, g.group_name, g.group_description, g.group_portrait, g.group_type, ",
//            "       g.create_time, g.update_time, g.group_admin ",
//            "FROM chat_group g ",
//            "LEFT JOIN chat_group_user_info gu ON g.group_id = gu.group_id ",
//            "WHERE 1=1 ",
//            "<if test='userId != null'>",
//            "    AND gu.user_id = #{userId} ",
//            "</if>",
//            "<if test='keyword != null and keyword != \"\"'>",
//            "    AND (g.group_name LIKE CONCAT('%', #{keyword}, '%') ",
//            "         OR g.group_description LIKE CONCAT('%', #{keyword}, '%')) ",
//            "</if>",
//            "GROUP BY g.group_id",
//            "</script>"
//    })
//    List<GroupInfoEntity> getJoinGroups(@Param("userId") Long userId,
//                            @Param("keyword") String keyword);

    @Select({
            "<script>",
            "SELECT g.group_id, g.group_name, g.group_description, g.group_portrait, ",
            "       g.group_type, g.create_time, g.update_time, g.group_admin, ",
            "       u.user_name AS group_admin_name, ",
            "       (SELECT COUNT(*) FROM chat_group_user_info WHERE group_id = g.group_id) AS group_member_count ",
            "FROM chat_group g ",
            "LEFT JOIN user_info u ON g.group_admin = u.user_id ",
            "LEFT JOIN chat_group_user_info cgui ON g.group_id = cgui.group_id ",
            "WHERE 1=1",
            "<if test='userId != null'>",
            "    AND cgui.user_id = #{userId} ",
            "</if>",
            "<if test='keyword != null and keyword != \"\"'>",
            "    AND (g.group_name LIKE CONCAT('%', #{keyword}, '%') ",
            "         OR g.group_description LIKE CONCAT('%', #{keyword}, '%') ",
            "         <if test='isNumeric'>",  // 仅当 isNumeric 为 true 时匹配 group_id
            "         OR g.group_id = #{keyword} ",
            "         </if>",
            "    )",
            "</if>",
            "GROUP BY g.group_id, u.user_name",
            "ORDER BY g.create_time DESC",
            "</script>"
    })
    Page<GroupDetailInfoEntity> getJoinGroups(@Param("userId") Long userId,
                                              @Param("keyword") String keyword,
                                              @Param("isNumeric") boolean isNumeric,
                                              IPage<GroupDetailInfoEntity> page);


    /**
     * 查询用户加入的群组（用于标签管理），支持 group_name 模糊搜索
     * 并且如果 keyword 是数字，支持 group_id 精准匹配
     */
    @Select({
            "<script>",
            "SELECT g.group_id, g.group_name",
            "FROM chat_group g ",
            "LEFT JOIN chat_group_user_info cgui ON g.group_id = cgui.group_id ",
            "WHERE 1=1",
            "<if test='userId != null'>",
            "    AND cgui.user_id = #{userId} ",
            "</if>",
            "<if test='keyword != null and keyword != \"\"'>",
            "    AND (g.group_name LIKE CONCAT('%', #{keyword}, '%') ",
            "         OR g.group_description LIKE CONCAT('%', #{keyword}, '%') ",
            "         <if test='isNumeric'>",
            "         OR g.group_id = #{keyword} ",
            "         </if>",
            "    )",
            "</if>",
            "GROUP BY g.group_id",
            "ORDER BY g.create_time DESC",
            "</script>"
    })
    List<GroupTagEntity> getJoinGroupsForTag(@Param("userId") Long userId,
                                             @Param("keyword") String keyword,
                                             @Param("isNumeric") boolean isNumeric);

    /**
     * 查询绑定了特定标签的群组，支持 group_name 模糊搜索
     * 并且如果 keyword 是数字，支持 group_id 精准匹配
     */
    @Select({
            "<script>",
            "SELECT g.group_id, g.group_name",
            "FROM chat_group g ",
            "LEFT JOIN chat_tag_group_relation ctgr ON g.group_id = ctgr.group_id ",
            "WHERE 1=1",
            "<if test='tagId != null'>",
            "    AND ctgr.tag_id = #{tagId} ",
            "</if>",
            "<if test='keyword != null and keyword != \"\"'>",
            "    AND (g.group_name LIKE CONCAT('%', #{keyword}, '%') ",
            "         <if test='isNumeric'>",
            "         OR g.group_id = #{keyword} ",
            "         </if>",
            "    )",
            "</if>",
            "GROUP BY g.group_id",
            "ORDER BY g.create_time DESC",
            "</script>"
    })
    List<GroupTagEntity> getGroupsBindedTag(@Param("tagId") Long tagId,
                                            @Param("keyword") String keyword,
                                            @Param("isNumeric") boolean isNumeric);
}
