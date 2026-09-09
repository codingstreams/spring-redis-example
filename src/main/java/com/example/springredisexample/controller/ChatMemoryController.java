package com.example.springredisexample.controller;

import com.example.springredisexample.controller.dto.ChatMessage;
import com.example.springredisexample.service.ChatMemoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
public class ChatMemoryController {
    private final ChatMemoryService chatMemoryService;

    @PostMapping("/session")
    public ResponseEntity<String> createSession() {
        final var sessionId = chatMemoryService.createSession();
        return ResponseEntity.ok(sessionId);
    }

    @PostMapping
    public ResponseEntity<ChatMessage> chat(@RequestBody final ChatMessage message) {
        final var response = chatMemoryService.chat(message);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{sessionId}")
    public ResponseEntity<List<ChatMessage>> getChatHistory(@PathVariable final String sessionId) {
        final var history = chatMemoryService.getChatHistory(sessionId);
        return ResponseEntity.ok(history);
    }
}
