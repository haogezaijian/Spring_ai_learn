package com.haoge.spring_ai_learn.controller;

import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.deepseek.DeepSeekChatModel;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ai")
public class AiController {
    private final ChatModel openAiChatModel ;
    private final ChatModel deepSeekChatModel;
    private final ChatModel ollamaChatModel;

    public AiController(DeepSeekChatModel deepSeekChatModel, OpenAiChatModel openAiChatModel, OllamaChatModel ollamaChatModel) {
        this.deepSeekChatModel = deepSeekChatModel;
        this.openAiChatModel = openAiChatModel;
        this.ollamaChatModel = ollamaChatModel;
    }

    /**
     *
     * @return the string
     */
    @GetMapping("/chat01")
    public String chat01() {
        return openAiChatModel.call(new UserMessage("讲一个笑话"), new SystemMessage("你是一个科学家，名叫：里斯"));
    }

    /**
     *
     * @return the string
     */
    @GetMapping("/chat02")
    public String chat02() {
        return deepSeekChatModel.call(new UserMessage("讲一个笑话"), new SystemMessage("你是一个物理家，名叫：章散"));
    }

    /**
     *
     * @return the string
     */
    @GetMapping("/chat03")
    public String chat03() {
        return ollamaChatModel.call(new UserMessage("讲一个笑话"), new SystemMessage("你是一个画家，名叫：汪乌"));
    }

//    /**
//     * 流式输出
//     *
//     * @param message the message
//     * @return the flux
//     */
//    @GetMapping("/chat02")
//    public Flux<String> chat02(@RequestParam(value = "message", defaultValue = "讲一个笑话") String message) {
//        Prompt prompt = new Prompt(new UserMessage(message),new SystemMessage("你是一个科学家，名叫：里斯"));
//        return chatClient.prompt(prompt).stream().content();
//    }
//
//    @GetMapping("/chat03")
//    public Flux<String> chat03(@RequestParam(value = "message", defaultValue = "讲一个笑话") String message) {
//        return chatClient.prompt()
//                .system(s -> s.text("你是一个科学家，名叫：{name}").param("name", "里斯"))
//                .user(u -> u.text(message))
//                .stream().content();
//    }
}
