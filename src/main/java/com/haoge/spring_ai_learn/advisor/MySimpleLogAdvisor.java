package com.haoge.spring_ai_learn.advisor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClientMessageAggregator;
import org.springframework.ai.chat.client.ChatClientRequest;
import org.springframework.ai.chat.client.ChatClientResponse;
import org.springframework.ai.chat.client.advisor.api.CallAdvisor;
import org.springframework.ai.chat.client.advisor.api.CallAdvisorChain;
import org.springframework.ai.chat.client.advisor.api.StreamAdvisor;
import org.springframework.ai.chat.client.advisor.api.StreamAdvisorChain;
import reactor.core.publisher.Flux;

@Slf4j
public class MySimpleLogAdvisor implements CallAdvisor, StreamAdvisor {
    @Override
    public ChatClientResponse adviseCall(ChatClientRequest chatClientRequest, CallAdvisorChain callAdvisorChain) {
        log.info("请求参数：{}", chatClientRequest);
        ChatClientResponse response = callAdvisorChain.nextCall(chatClientRequest);
        log.info("响应结果：{}", response);
        return response;
    }

    @Override
    public Flux<ChatClientResponse> adviseStream(ChatClientRequest chatClientRequest, StreamAdvisorChain streamAdvisorChain) {
        log.info("请求参数：{}", chatClientRequest);
        Flux<ChatClientResponse> responses = streamAdvisorChain.nextStream(chatClientRequest);
        return new ChatClientMessageAggregator().aggregateChatClientResponse(responses, response -> log.info("响应结果：{}", response));
    }

    @Override
    public String getName() {
        return MySimpleLogAdvisor.class.getName();
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
