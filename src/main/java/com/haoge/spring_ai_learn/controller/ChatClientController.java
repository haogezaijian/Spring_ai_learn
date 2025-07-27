package com.haoge.spring_ai_learn.controller;

import com.haoge.spring_ai_learn.advisor.MySimpleLogAdvisor;
import com.haoge.spring_ai_learn.converter.ActorsFilms;
import com.haoge.spring_ai_learn.tool.DateTimeTools;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.util.List;

@RestController
@RequestMapping("/client/ai")
@Slf4j
public class ChatClientController {

    @Resource
    private ChatClient openAiChatClient;

    @Resource
    private ChatClient deepSeekChatClient;

    @Resource
    private ChatClient ollamaChatClient;


    @GetMapping("/openai/chat")
    public String openAiChat() {
        Prompt prompt = new Prompt(new UserMessage("讲一个笑话"), new SystemMessage("你是一个物理家，名叫：章散") );
        return openAiChatClient.prompt(prompt).advisors(new MySimpleLogAdvisor()).call().content();
    }

    /**
     * 流式输出
     *
     * @return the flux
     */
    @GetMapping(value = "/deepseek/chat")
    public Flux<String> deepSeekChat() {
        Prompt prompt = new Prompt(new UserMessage("讲一个笑话"), new SystemMessage("你是一个物理家，名叫：里斯") );
        return deepSeekChatClient.prompt(prompt).advisors(new MySimpleLogAdvisor()).stream().content();
    }

    @GetMapping("/ollama/chat")
    public Flux<String> ollamaChat(String role,String name) {
        return ollamaChatClient.prompt()
                .system(s -> s.text("你是一个{role}，名叫：{name}")
                        .param("name", name)
                        .param("role", role))
                .user("你是谁，你叫什么")
                .stream().content();
    }

    //保留最近两条聊天记录
    MessageWindowChatMemory memory = MessageWindowChatMemory.builder()
            .maxMessages(3)
            .build();
//    @GetMapping("/memory/chat")
//    public String memoryChat(String message,String chatId) {
//
//        UserMessage userMessage = new UserMessage(message);
//        memory.add(chatId,userMessage);
//        ChatResponse chatResponse = deepSeekChatClient.prompt(new Prompt(memory.get(chatId))).call().chatResponse();
//        memory.add(chatId,chatResponse.getResult().getOutput());
//
//        return chatResponse.getResult().getOutput().getText();
//    }

    @GetMapping("/memory/chat")
    public String memoryChat(String message,String chatId) {
        ChatResponse chatResponse = deepSeekChatClient
                .prompt(message)
                .advisors(MessageChatMemoryAdvisor.builder(memory).build())
                .advisors(a -> a.param(ChatMemory.CONVERSATION_ID, chatId))
                .call().chatResponse();
        return chatResponse.getResult().getOutput().getText();
    }

    @GetMapping("/converter/chat")
    public void converterChat() {
        ActorsFilms actorsFilms = deepSeekChatClient.prompt()
                .user(u -> u.text("生成5部{actor}主演的电影")
                        .param("actor", "成龙"))
                .call()
                .entity(ActorsFilms.class);
        System.out.println(actorsFilms);

        // 可以转换为对象列表
        List<ActorsFilms> actorsFilmsList = deepSeekChatClient.prompt()
                .user("分别生成由成龙，李连杰，周润发主演的5部电影")
                .call()
                .entity(new ParameterizedTypeReference<List<ActorsFilms>>() {});
        log.info("列表结果：{}", actorsFilmsList);

    }

    @GetMapping("/tools/chat")
    public void toolsChat() {
        String response = openAiChatClient
                .prompt("What day is tomorrow?")
                .tools(new DateTimeTools())
                .call()
                .content();

        System.out.println(response);
    }
}
