package com.example.springredisexample.controller.dto;

import java.io.Serializable;

public record ChatMessage(ChatMessage.Role role, String content, String sessionId) implements Serializable {
  public enum Role {
    USER, AI
  }
}
