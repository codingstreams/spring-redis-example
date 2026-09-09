package com.example.springredisexample.repo;

import com.example.springredisexample.model.Transaction;
import org.springframework.data.jpa.domain.Specification;

import java.util.UUID;

public interface TransactionSpecification {
   static Specification<Transaction> withFilters(UUID userId, TransactionFilterParams filters) {
    return (root, query, cb) -> {

      var spec = cb.equal(root.get("appUser").get("id"), userId);

      if (filters.type() != null && !filters.type().isBlank()) {
        spec = cb.and(spec, cb.equal(root.get("type"), Transaction.TransactionType.valueOf(filters.type().toUpperCase())));
      }

      if (filters.category() != null && !filters.category().isBlank()) {
        spec = cb.and(spec, cb.equal(root.get("transactionCategory").get("name"), filters.category()));
      }

      if (filters.startDate() != null) {
        spec = cb.and(spec, cb.greaterThanOrEqualTo(root.get("transactionDate"), filters.startDate()));
      }
      if (filters.endDate() != null) {
        spec = cb.and(spec, cb.lessThanOrEqualTo(root.get("transactionDate"), filters.endDate()));
      }

      if (filters.minAmount() != null) {
        spec = cb.and(spec, cb.greaterThanOrEqualTo(root.get("amount"), filters.minAmount()));
      }
      if (filters.maxAmount() != null) {
        spec = cb.and(spec, cb.lessThanOrEqualTo(root.get("amount"), filters.maxAmount()));
      }

      return spec;
    };
  }
}
