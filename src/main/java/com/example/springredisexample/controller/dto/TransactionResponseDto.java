package com.example.springredisexample.controller.dto;

import com.example.springredisexample.model.Transaction;

import java.time.LocalDate;
import java.util.UUID;

public record TransactionResponseDto(
    UUID id,
    Transaction.TransactionType type,
    Float amount,
    LocalDate transactionDate,
    String description,
    String account,
    String paymentMode,
    String category
) {
}