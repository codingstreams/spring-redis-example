package com.example.springredisexample.service;

import com.example.springredisexample.controller.dto.FinancialSummaryDTO;
import com.example.springredisexample.model.Transaction;
import com.example.springredisexample.repo.TransactionRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccountService {
  private final TransactionRepo transactionRepo;

  @Cacheable(
      value = "financial_summaries",
      key = "#accountId",
      unless = "#result == null"
  )
  public FinancialSummaryDTO getFinancialSummary(final UUID accountId) {
    final var transactions = transactionRepo.findAll((root, query, cb) -> cb.equal(root.get("account").get("id"), accountId));
    final var totalBalance = transactions.stream()
        .filter(t -> t.getAmount() != null)
        .map(t -> t.getType() == Transaction.TransactionType.EXPENSE
            ? BigDecimal.valueOf(t.getAmount()).negate()
            : BigDecimal.valueOf(t.getAmount()))
        .reduce(BigDecimal.ZERO, BigDecimal::add);
    final var currency = "INR";
    final var lastUpdated = LocalDateTime.now();

    return new FinancialSummaryDTO(accountId, totalBalance, currency, lastUpdated);
  }

  @CacheEvict(value = "financial_summaries", key = "#accountId")
  public void invalidateSummary(final UUID accountId) {
    final var message = "Cache evicted for account: " + accountId;
    log.info(message);
  }
}