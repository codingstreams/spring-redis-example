package com.example.springredisexample.controller;

import com.example.springredisexample.controller.dto.AccountDto;
import com.example.springredisexample.controller.dto.FinancialSummaryDTO;
import com.example.springredisexample.controller.dto.PagedTransactionsDto;
import com.example.springredisexample.repo.TransactionFilterParams;
import com.example.springredisexample.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping
    public ResponseEntity<PagedTransactionsDto> getAllTransactions(@ModelAttribute TransactionFilterParams filterParams, Pageable pageable) {
        final var userId = "c8f69497-9c4d-406e-8d2f-0afe09a155f3";
        return ResponseEntity.ok(accountService.getAllTransactions(userId, filterParams, pageable));
    }

    @GetMapping("/{accountId}/details")
    public ResponseEntity<AccountDto> getAccount(@PathVariable final UUID accountId) {
        return ResponseEntity.ok(accountService.getAccount(accountId));
    }
}

