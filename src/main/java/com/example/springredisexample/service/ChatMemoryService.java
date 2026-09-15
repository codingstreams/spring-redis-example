package com.example.springredisexample.service;

import com.example.springredisexample.controller.dto.ChatMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ChatMemoryService {
  private static final String CHAT_KEY_PREFIX = "ai:chat:history:";

  private final ChatClient chatClient;
  private final RedisTemplate<String, ChatMessage> redisTemplate;

  public String createSession() {
    final var sessionId = UUID.randomUUID().toString();
    return sessionId;
  }

  public void appendMessage(final String sessionId, final ChatMessage message) {
    final var redisKey = CHAT_KEY_PREFIX + sessionId;
    redisTemplate.opsForList().rightPush(redisKey, message);
    redisTemplate.opsForList().trim(redisKey, -3, -1);
    redisTemplate.expire(redisKey, Duration.ofDays(7));
  }

  public List<ChatMessage> getChatHistory(final String sessionId) {
    final var redisKey = CHAT_KEY_PREFIX + sessionId;
    final var history = redisTemplate.opsForList().range(redisKey, 0, -1);
    return history != null ? history : List.of();
  }

  public ChatMessage chat(final ChatMessage message) {
    final var sessionId = message.sessionId() != null ? message.sessionId() : createSession();
    final var history = getChatHistory(sessionId);
    final var historyMessages = new ArrayList<Message>();

    history.stream()
        .map(m -> m.role() == ChatMessage.Role.USER
            ? new UserMessage(m.content())
            : new AssistantMessage(m.content()))
        .forEach(historyMessages::add);

    final var content = chatClient.prompt()
        .messages(historyMessages)
        .user(message.content())
        .call()
        .content();

    final var userMessage = new ChatMessage(ChatMessage.Role.USER, message.content(), sessionId);
    appendMessage(sessionId, userMessage);

    final var response = new ChatMessage(ChatMessage.Role.AI, content, sessionId);
    appendMessage(sessionId, response);
    return response;
  }
}