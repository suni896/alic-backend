package com.eduhk.alic.alicbackend.controller;

import com.eduhk.alic.alicbackend.model.vo.*;
import com.eduhk.alic.alicbackend.service.GroupSearchService;
import com.eduhk.alic.alicbackend.service.TagManageService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author FuSu
 * @date 2025/1/21 14:58
 */
@RestController
@Slf4j
@RequestMapping("/v1/tag")
public class TagController {
    @Resource
    TagManageService tagManageService;

    @Resource
    GroupSearchService groupSearchService;

    @PostMapping("/add_tag")
    public Result addTag(@Validated @RequestBody TagNameVO TagName, @RequestAttribute("userId") Long userId) {
        Long tagId = tagManageService.createNewTag(TagName.getTagName(), userId);
        TagIdVO tagIdVO = new TagIdVO();
        tagIdVO.setTagId(tagId);
        return ResultResp.success(tagIdVO);
    }

    @PostMapping("/delete_tag")
    public Result deleteTag(@Validated @RequestBody TagIdVO tagId, @RequestAttribute("userId") Long userId) {
        tagManageService.verifyUserAdmission(userId, tagId.getTagId());
        tagManageService.deleteTagById(tagId.getTagId());
        return ResultResp.success(tagId);
    }

    @PostMapping("/add_group")
    public Result addGroupToTag(@Validated @RequestBody TagGroupInfoVO tagGroupInfoVO, @RequestAttribute("userId") Long userId) {
        tagManageService.verifyUserAdmission(userId, tagGroupInfoVO.getTagId());
        tagManageService.addGroup(tagGroupInfoVO);
        return ResultResp.success();
    }

    @PostMapping("/remove_group")
    public Result removeGroupFromTag(@Validated @RequestBody TagGroupInfoVO tagGroupInfoVO, @RequestAttribute("userId") Long userId) {
        tagManageService.verifyUserAdmission(userId, tagGroupInfoVO.getTagId());
        tagManageService.removeGroup(tagGroupInfoVO);
        return ResultResp.success();
    }

    @GetMapping("/get_tag_list")
    public Result getTagList(@RequestAttribute("userId") Long userId) {
        List<TagInfoVO> tagInfoVOList = tagManageService.getTagList(userId);
        return ResultResp.success(tagInfoVOList);
    }

    @GetMapping("/get_tag_info")
    public Result getTagInfo(@Validated @RequestParam @NotNull(message = "tagId cannot be null")
                                 @Min(value = 1, message = "tagId must be greater than 0") Long tagId, @RequestAttribute("userId") Long userId) {
        tagManageService.verifyUserAdmission(userId, tagId);
        List<TagGroupVO> tagGroupVOS = tagManageService.getGroupBindTagList(tagId);
        return ResultResp.success(tagGroupVOS);
    }

    @PostMapping("/get_group_list_for_tag")
    public Result getGroupList(@Validated @RequestBody GroupTagSearchVO groupTagSearchVO, @RequestAttribute("userId") Long userId) {
        tagManageService.verifyUserAdmission(userId, groupTagSearchVO.getTagId());
        return ResultResp.success(groupSearchService.searchGroupForTag(userId, groupTagSearchVO.getTagId(),groupTagSearchVO.getKeyword()));
    }
}
