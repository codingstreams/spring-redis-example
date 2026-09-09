package com.example.springredisexample.repo;

import com.example.springredisexample.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TransactionRepo extends JpaRepository<Transaction, UUID>, JpaSpecificationExecutor<Transaction> {
  Optional<Transaction> findByIdAndAppUserId(UUID transactionId, UUID userUuid);

  List<Transaction> findAllByTransferIdAndAppUserId(UUID transferId, UUID userUuid);
}
