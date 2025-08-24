package com.haoge.spring_ai_learn.config;

import com.haoge.spring_ai_learn.tool.WeatherService;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.tool.method.MethodToolCallbackProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ToolCallbackProviderConfig {

    @Bean
    public ToolCallbackProvider weatherCallbackProvider(WeatherService weatherService) {
        return MethodToolCallbackProvider
                .builder()
                .toolObjects(weatherService)
                .build();
    }
}
