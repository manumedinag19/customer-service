package com.example.customerservice.adapters.inbound.rest.mapper;

import com.example.customerservice.adapters.inbound.rest.dto.AccountRequest;
import com.example.customerservice.adapters.inbound.rest.dto.AccountResponse;
import com.example.customerservice.adapters.inbound.rest.dto.CustomerRequest;
import com.example.customerservice.adapters.inbound.rest.dto.CustomerResponse;
import com.example.customerservice.domain.model.Account;
import com.example.customerservice.domain.model.Customer;

public class RestMapper {

    public static Customer toDomain(CustomerRequest req) {
        return new Customer(null, req.getFullName(), req.getEmail(), req.getDocumentType(), req.getDocumentNumber(), null);
    }

    public static CustomerResponse toCustomerResponse(Customer c) {
        return new CustomerResponse(c.getId(), c.getFullName(), c.getDocumentType(), c.getDocumentNumber(), c.getEmail(), c.getCreatedAt());
    }

    public static Account toDomain(AccountRequest req) {
        return new Account(null, req.getCustomerId(), req.getNumber(), req.getInitialBalance(), req.getType(), null);
    }

    public static AccountResponse toAccountResponse(Account a) {
        return new AccountResponse(a.getId(), a.getCustomerId(), a.getNumber(), a.getBalance(), a.getType(), a.getCreatedAt());
    }
}
