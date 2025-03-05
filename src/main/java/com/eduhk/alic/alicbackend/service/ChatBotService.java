package com.eduhk.alic.alicbackend.service;

import com.eduhk.alic.alicbackend.common.constant.ResultCode;
import com.eduhk.alic.alicbackend.common.exception.BaseException;
import com.eduhk.alic.alicbackend.dao.ChatGroupBotMapper;
import com.eduhk.alic.alicbackend.model.entity.ChatBotInfoEntity;
import com.eduhk.alic.alicbackend.model.vo.ChatBotDemonVO;
import com.eduhk.alic.alicbackend.model.vo.ChatBotVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.util.retry.Retry;

import javax.annotation.Resource;
import java.time.Duration;
import java.util.List;
import java.util.Objects;

/**
 * @author FuSu
 * @date 2025/3/5 17:03
 */
@Service
@Slf4j
public class ChatBotService {
    @Resource
    private ChatGroupBotMapper chatGroupBotMapper;
    @Resource
    private BotManagementService botManagementService;

    @Transactional
    public void modifyGroupBot(ChatBotDemonVO chatBotModifyVO, ChatBotInfoEntity chatBotInfoEntity) {
        ChatBotInfoEntity modifyChatBotInfoEntity = new ChatBotInfoEntity();
        modifyChatBotInfoEntity.setBotId(chatBotModifyVO.getBotId());
        modifyChatBotInfoEntity.setAccessType(chatBotModifyVO.getAccessType());
        modifyChatBotInfoEntity.setBotContext(chatBotModifyVO.getBotContext());
        modifyChatBotInfoEntity.setBotPrompt(chatBotModifyVO.getBotPrompt());

        chatGroupBotMapper.update(modifyChatBotInfoEntity);

        if (!Objects.equals(chatBotInfoEntity.getBotPrompt(), chatBotModifyVO.getBotPrompt())) {
            log.info("修改prompt:"+chatBotModifyVO.getBotPrompt());
            modifyAssistantAsync(chatBotInfoEntity.getAgentId(), chatBotModifyVO.getBotPrompt());
        }
    }
    /**
     * **同步**插入数据库 + **异步**调用 Azure API
     */
    @Transactional
    public void createGroupBot(List<ChatBotVO> chatBotVOList, long groupId) {
        log.info("开始创建 Group Bot，Group ID: {}", groupId);

        List<ChatBotInfoEntity> chatBotInfoEntities = chatBotVOList.stream()
                .map(vo -> {
                    ChatBotInfoEntity entity = new ChatBotInfoEntity();
                    entity.setBotName(vo.getBotName());
                    entity.setBotPrompt(vo.getBotPrompt());
                    entity.setBotContext(vo.getBotContext());
                    entity.setAccessType(vo.getAccessType());
                    entity.setGroupId(groupId);
                    entity.setBotCondition(0L);
                    return entity;
                })
                .toList();
        Long duplicateBotNum = chatGroupBotMapper.batchQueryByGroupIdAndBotNameAndCondition(chatBotInfoEntities);
        if (duplicateBotNum > 0) {
            throw new BaseException(ResultCode.BOT_NAME_DUPLICATE);
        }
        // **同步插入数据库**
        chatGroupBotMapper.insertBatch(chatBotInfoEntities);
        log.info("数据库插入成功，Group ID: {}", groupId);

        // **异步调用 Azure API**
        chatBotInfoEntities.forEach(this::createAssistantAsync);
    }

    /**
     * **异步调用 Azure OpenAI API**，失败重试
     */
    @Async
    public void createAssistantAsync(ChatBotInfoEntity entity) {
        log.info("开始创建 Assistant，Bot Name: {}", entity.getBotName());

        botManagementService.createAssistant(entity.getBotPrompt())
                .retryWhen(Retry.backoff(3, Duration.ofSeconds(2))) // **失败重试3次，每次间隔2秒**
                .subscribe(
                        assistantId -> {
                            log.info("Assistant 创建成功: {}, ID: {}", entity.getBotName(), assistantId);
                            // **成功后更新数据库**
                            chatGroupBotMapper.updateAgentId(entity.getBotId(), assistantId);
                        },
                        error -> {
                            log.error("Assistant 创建失败，Bot Name: {}，错误: {}", entity.getBotName(), error.getMessage());
                        }
                );
    }

    @Async
    public void modifyAssistantAsync(String agentId, String prompt) {
        log.info("开始修改 Assistant，agentId: {}", agentId);

        botManagementService.modifyAssistant(agentId, prompt)
                .retryWhen(Retry.backoff(3, Duration.ofSeconds(2))).block(); // **失败重试3次，每次间隔2秒**
    }
}
