package com.example.springredisexample.controller.dto;

import org.springframework.data.domain.Page;

import java.util.List;

public record PagedTransactionsDto(
        List<TransactionResponseDto> content,
        int pageNumber,
        int pageSize,
        long totalElements,
        int totalPages,
        boolean isLast
) {
  public static PagedTransactionsDto from(Page<TransactionResponseDto> page) {
    return new PagedTransactionsDto(
        page.getContent(),
        page.getNumber(),
        page.getSize(),
        page.getTotalElements(),
        page.getTotalPages(),
        page.isLast()
    );
  }
}