package com.example.customerservice.adapters.outbound.persistence.mapper;

import com.example.customerservice.adapters.outbound.persistence.entity.AccountEntity;
import com.example.customerservice.adapters.outbound.persistence.entity.CustomerEntity;
import com.example.customerservice.domain.model.Account;
import com.example.customerservice.domain.model.Customer;

import java.time.Instant;

public class PersistenceMapper {

    public static CustomerEntity toEntity(Customer c) {
        Instant created = c.getCreatedAt() == null ? Instant.now() : c.getCreatedAt();
        return new CustomerEntity(c.getId(), c.getFullName(), c.getEmail(), c.getDocumentNumber(), c.getDocumentType(), created);
    }

    public static Customer toDomain(CustomerEntity e) {
        return new Customer(e.getId(), e.getFullName(), e.getEmail(), e.getDocumentType(), e.getDocumentNumber(), e.getCreatedAt());
    }

    public static AccountEntity toEntity(Account a) {
        Instant created = a.getCreatedAt() == null ? Instant.now() : a.getCreatedAt();
        return new AccountEntity(a.getId(), a.getCustomerId(), a.getNumber(), a.getBalance(), a.getType(), created);
    }

    public static Account toDomain(AccountEntity e) {
        return new Account(e.getId(), e.getCustomerId(), e.getNumber(), e.getBalance(), e.getType(), e.getCreatedAt());
    }
}
