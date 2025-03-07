package com.eduhk.alic.alicbackend.task;

import com.eduhk.alic.alicbackend.dao.ChatGroupBotMapper;
import com.eduhk.alic.alicbackend.model.entity.ChatBotInfoEntity;
import com.eduhk.alic.alicbackend.service.BotManagementService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import reactor.util.retry.Retry;
import javax.annotation.Resource;
import java.time.Duration;
import java.util.Date;
import java.util.List;

import static com.sun.mail.imap.protocol.INTERNALDATE.format;

/**
 * @author FuSu
 * @date 2025/3/7 15:15
 */
@Component
@Slf4j
public class ChatBotAsyncTask {
    @Resource
    private ChatGroupBotMapper chatGroupBotMapper;
    @Resource
    private BotManagementService botManagementService;

    @Async("taskExecutor")
    @Scheduled(fixedDelay = 30000) // 每 10 秒执行一次
    public void processPendingBots() {
        log.info(Thread.currentThread().getName() + " >>> 定时任务执行开始 " + format(new Date()));
        List<ChatBotInfoEntity> pendingCreateBots = chatGroupBotMapper.selectByStatus(0L); // bot_status = 0 (创建中)
        for (ChatBotInfoEntity bot : pendingCreateBots) {
            try {
                String assistantId = botManagementService.createAssistant(bot.getBotPrompt())
                        .retryWhen(Retry.backoff(3, Duration.ofSeconds(2)))
                        .block();
                log.info("Azure Assistant 创建成功：" + bot.getBotId());
                chatGroupBotMapper.updateAgentId(bot.getBotId(), assistantId);
            } catch (Exception e) {
                chatGroupBotMapper.updateBotStatus(bot.getBotId(), 3L); // 更新为 "失败"
                log.error("Azure Assistant 创建失败：" + bot.getBotId(), e);
            }
        }

        List<ChatBotInfoEntity> pendingModifyBots = chatGroupBotMapper.selectByStatus(2L); // bot_status = 2 (修改中)
        for (ChatBotInfoEntity bot : pendingModifyBots) {
            try {
                botManagementService.modifyAssistant(bot.getAgentId(), bot.getBotPrompt())
                        .retryWhen(Retry.backoff(3, Duration.ofSeconds(2))) // 失败重试 3 次，每次间隔 2 秒
                        .block();
                chatGroupBotMapper.updateBotStatus(bot.getBotId(), 1L);
                log.info("Azure Assistant 更新成功：" + bot.getBotId());
            } catch (Exception e) {
                chatGroupBotMapper.updateBotStatus(bot.getBotId(), 3L); // 更新为 "失败"
                log.error("Azure Assistant 更新失败：" + bot.getBotId(), e);
            }
        }

    }
}
