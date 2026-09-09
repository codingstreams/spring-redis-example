package com.example.springredisexample.repo;

import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

public record TransactionFilterParams(
    String type,
    String category,
    
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    LocalDate startDate,
    
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    LocalDate endDate,
    
    Float minAmount,
    Float maxAmount
) {
    public static TransactionFilterParams empty() {
        return new TransactionFilterParams(null, null, null, null, null, null);
    }

  public static TransactionFilterParams dateRange(LocalDate startDate, LocalDate endDate) {
    return new TransactionFilterParams(null, null, startDate, endDate, null, null);
  }
}