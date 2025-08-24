package com.haoge.spring_ai_learn.controller;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/rag")
@Slf4j
public class RagController {

    @Resource
    private ChatClient openAiChatClient;

    @Resource
    private VectorStore vectorStore;

    @RequestMapping("/chatWithRag")
    public String chatWithRag(String message,String chatId) {
        ChatResponse response = openAiChatClient.prompt()
                .user(message)
                .advisors(spec -> spec.param(ChatMemory.CONVERSATION_ID, chatId))
                .advisors(new QuestionAnswerAdvisor(vectorStore))
                .call()
                .chatResponse();
        String result = response.getResult().getOutput().getText();
        log.info("result: {}", result);

        return result;
    }

    @RequestMapping("/chat")
    public String chat(String message,String chatId) {
        ChatResponse response = openAiChatClient.prompt()
                .user(message)
                .call()
                .chatResponse();
        String result = response.getResult().getOutput().getText();
        log.info("result: {}", result);
        return result;
    }
}
