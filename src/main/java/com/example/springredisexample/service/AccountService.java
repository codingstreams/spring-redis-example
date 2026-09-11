package com.example.springredisexample.service;

import com.example.springredisexample.config.RedisConfig;
import com.example.springredisexample.controller.dto.AccountDto;
import com.example.springredisexample.controller.dto.FinancialSummaryDTO;
import com.example.springredisexample.controller.dto.PagedTransactionsDto;
import com.example.springredisexample.controller.dto.TransactionResponseDto;
import com.example.springredisexample.model.Account;
import com.example.springredisexample.model.PaymentMode;
import com.example.springredisexample.model.SystemCategory;
import com.example.springredisexample.model.Transaction;
import com.example.springredisexample.repo.AccountRepo;
import com.example.springredisexample.repo.TransactionFilterParams;
import com.example.springredisexample.repo.TransactionRepo;
import com.example.springredisexample.repo.TransactionSpecification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccountService {
  private final TransactionRepo transactionRepo;
  private final AccountRepo accountRepo;

  @Cacheable(
      value = RedisConfig.CACHE_FINANCIAL_SUMMARIES,
      key = "#accountId",
      unless = "#result == null"
  )
  public FinancialSummaryDTO getFinancialSummary(final UUID accountId) {
    final var transactions = transactionRepo.findAll((root, _, cb) -> cb.equal(root.get("account").get("id"), accountId));
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

  @CacheEvict(value = RedisConfig.CACHE_FINANCIAL_SUMMARIES, key = "#accountId")
  public void invalidateSummary(final UUID accountId) {
    final var message = "Cache evicted for account: " + accountId;
    log.info(message);
  }

  @Cacheable(
      value = "userTransactions",
      key = "#userId + ':' + #pageable.pageNumber"
  )
  public PagedTransactionsDto getAllTransactions(String userId, TransactionFilterParams filterParams, Pageable pageable) {
    final var parsedUserId = UUID.fromString(userId);
    final var spec = TransactionSpecification.withFilters(parsedUserId, filterParams);

    final var page = transactionRepo.findAll(spec, pageable)
        .map(AccountService::toDto);

    return PagedTransactionsDto.from(page);
  }

  public static TransactionResponseDto toDto(Transaction t) {
    return new TransactionResponseDto(
        t.getId(),
        t.getType(),
        t.getAmount(),
        t.getTransactionDate(),
        t.getDescription(),
        null,
        Optional.ofNullable(t.getPaymentMode())
            .map(PaymentMode::getName)
            .orElse(""),
        Optional.ofNullable(t.getTransactionCategory())
            .map(SystemCategory::getName)
            .orElse("")
    );
  }

  @Cacheable(value = "usersAccounts", key = "#accountId")
  public AccountDto getAccount(UUID accountId) {
    return accountRepo.findById(accountId)
        .map(AccountService::toAccountDto).orElseThrow();
  }

  private static AccountDto toAccountDto(Account account) {
    return new AccountDto(
        account.getId(),
        account.getBalance(),
        account.getAccountType()
    );
  }
}