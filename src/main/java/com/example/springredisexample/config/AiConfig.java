package com.example.springredisexample.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AiConfig {
  @Bean
  ChatClient chatClient(ChatClient.Builder chatClientBuilder) {
    return  chatClientBuilder.build();
  }
}
