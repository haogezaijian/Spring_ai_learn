package com.haoge.spring_ai_learn.controller;

import com.haoge.spring_ai_learn.advisor.MySimpleLogAdvisor;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/mcp")
@Slf4j
public class McpController {

    @Resource
    private ChatClient openAiChatClient;

    @Resource(name = "mcpToolCallbacks")
    private ToolCallbackProvider toolCallbackProvider;

    @Resource(name = "weatherCallbackProvider")
    private ToolCallbackProvider weatherCallbackProvider;

    @RequestMapping("/chat")
    public String chat(String  message) {
        ChatResponse response = openAiChatClient
                .prompt()
                .user(message)
                .call()
                .chatResponse();
        String result = response.getResult().getOutput().getText();
        log.info("result: {}", result);
        return result;
    }

    @RequestMapping("/chatWithMcp")
    public String chatWithMcp(String  message) {
        ChatResponse response = openAiChatClient
                .prompt()
                .user(message)
                .advisors(new MySimpleLogAdvisor())
                .toolCallbacks(weatherCallbackProvider)
                .call()
                .chatResponse();
        String result = response.getResult().getOutput().getText();
        log.info("result: {}", result);
        return result;
    }
}
