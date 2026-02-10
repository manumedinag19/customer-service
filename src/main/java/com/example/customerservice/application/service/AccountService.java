package com.example.customerservice.application.service;

import com.example.customerservice.domain.model.Account;

import java.util.List;
import java.util.UUID;

public interface AccountService {
    Account createAccount(Account account);
    List<Account> listAccounts(UUID customerId);
}

