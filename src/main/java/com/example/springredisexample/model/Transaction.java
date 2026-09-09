package com.example.springredisexample.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class Transaction extends BaseAudit {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @Enumerated(EnumType.STRING)
  private Transaction.TransactionType type;

  private Float amount;

  private LocalDate transactionDate;

  private UUID transferId;

  private String description;

  @ManyToOne(fetch = FetchType.EAGER)
  private SystemCategory transactionCategory;

  @ManyToOne(fetch = FetchType.LAZY)
  private AppUser appUser; // Owner

  @ManyToOne(fetch = FetchType.EAGER)
  private Account account;

  @ManyToOne(fetch = FetchType.EAGER)
  private PaymentMode paymentMode;

  public enum TransactionType {
    EXPENSE, INCOME, TRANSFER
  }
}
