package com.example.springredisexample.model;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.util.UUID;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class Account extends BaseAudit implements Serializable {
  @Serial
  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @Column(name = "balance", nullable = false)
  private Float balance;

  @Column(name = "account_type", nullable = false)
  @Enumerated(EnumType.STRING)
  private Account.AccountType accountType;

  @ManyToOne(fetch = FetchType.LAZY)
  @ToString.Exclude
  private AppUser appUser;

  public enum AccountType {
    SAVINGS, CREDIT, CASH
  }
}