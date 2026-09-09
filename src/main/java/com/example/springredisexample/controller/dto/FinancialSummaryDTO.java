package com.example.springredisexample.controller.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record FinancialSummaryDTO(
        UUID accountId,
        BigDecimal totalBalance,
        String currency,
        LocalDateTime lastUpdated
) implements Serializable {}