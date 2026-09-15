package com.example.springredisexample.controller.dto;

import com.example.springredisexample.model.Account;

import java.util.UUID;

public record AccountDto(
    UUID id,
    Float balance,
    Account.AccountType accountType
) {}