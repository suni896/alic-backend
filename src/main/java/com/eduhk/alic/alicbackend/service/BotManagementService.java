package com.eduhk.alic.alicbackend.service;

import com.eduhk.alic.alicbackend.model.vo.CreateAssistantRequestVO;
import com.eduhk.alic.alicbackend.model.vo.CreateAssistantResponseVO;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

/**
 * @author FuSu
 * @date 2025/3/4 13:11
 */
@Service
@Slf4j
public class BotManagementService {
    private static final String AZURE_OPENAI_ENDPOINT = "https://basgpt.openai.azure.com";
    private static final String API_VERSION = "2024-05-01-preview";
    private static final String API_KEY = "";
    private static final String model = "EDUAI";
    private static final WebClient webClient = WebClient.builder()
            .baseUrl(AZURE_OPENAI_ENDPOINT)
            .defaultHeader("Content-Type", "application/json")
            .defaultHeader("api-key", API_KEY)
            .build();

    public Mono<String> createAssistant(String prompt) {
        CreateAssistantRequestVO request = new CreateAssistantRequestVO();
        request.setInstructions(prompt);
        request.setModel(model);
        ObjectMapper objectMapper = new ObjectMapper();
        return webClient.post()
                .uri("/openai/assistants?api-version=" + API_VERSION)
                .bodyValue(request)
                .retrieve()
                .bodyToMono(CreateAssistantResponseVO.class)
                .map(CreateAssistantResponseVO::getId)
                .doOnError(error -> log.error("Assistant 创建失败: {}", error.getMessage()));
    }

    public Mono<Void> deleteAssistant(String assistantId) {

        return webClient.delete()
                .uri("/openai/assistants/" + assistantId + "?api-version=" + API_VERSION)
                .retrieve()
                .bodyToMono(Void.class)
                .doOnSuccess(unused -> log.info("Deleted Assistant ID: " + assistantId))
                .doOnError(error -> log.error("Failed to delete Assistant ID: " + assistantId, error));
    }

    public Mono<String> modifyAssistant(String assistantId, String prompt) {
        String requestBody = """
        {
            "instructions": "%s"
        }
        """.formatted(prompt, model);
        return webClient.post().uri("/openai/assistants/" + assistantId + "?api-version=" + API_VERSION).bodyValue(requestBody)
                .retrieve()
                .bodyToMono(CreateAssistantResponseVO.class)
                .map(CreateAssistantResponseVO::getId)
                .doOnError(error -> log.error("Assistant 修改失败: {}", error.getMessage()));
    }

}
