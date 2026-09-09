package com.example.springredisexample.controller;

import com.example.springredisexample.controller.dto.FinancialSummaryDTO;
import com.example.springredisexample.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/financial")
@RequiredArgsConstructor
public class FinancialSummaryController {

    private final AccountService accountService;

    @GetMapping("/{accountId}")
    public ResponseEntity<FinancialSummaryDTO> getFinancialSummary(@PathVariable final UUID accountId) {
        final var summary = accountService.getFinancialSummary(accountId);
        return ResponseEntity.ok(summary);
    }

    @DeleteMapping("/{accountId}")
    public ResponseEntity<Void> invalidateSummary(@PathVariable final UUID accountId) {
        accountService.invalidateSummary(accountId);
      return ResponseEntity.noContent().build();
    }
}

