package com.example.customerservice.domain.port.outbound;

import com.example.customerservice.domain.model.Account;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AccountRepository {
    Account save(Account account);
    List<Account> findAll();
    List<Account> findAllByCustomerId(UUID customerId);
    Optional<Account> findById(UUID id);
}

