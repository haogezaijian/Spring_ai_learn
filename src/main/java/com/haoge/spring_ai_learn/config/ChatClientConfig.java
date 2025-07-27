package com.haoge.spring_ai_learn.config;


import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.deepseek.DeepSeekChatModel;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ChatClientConfig {

    @Bean("openAiChatClient")
    public ChatClient openAiChatClient(OpenAiChatModel openAiChatModel) {
        return ChatClient.create(openAiChatModel);
    }

    @Bean("deepSeekChatClient")
    public ChatClient deepSeekChatClient(DeepSeekChatModel deepSeekChatModel) {
        return ChatClient.create(deepSeekChatModel);
    }

    @Bean("ollamaChatClient")
    public ChatClient ollamaChatModel(OllamaChatModel ollamaChatModel) {
        return ChatClient.create(ollamaChatModel);
    }
}
