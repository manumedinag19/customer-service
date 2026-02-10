package com.example.customerservice.application.service;

import com.example.customerservice.domain.model.Account;
import com.example.customerservice.domain.model.Customer;

import java.util.List;

public interface CustomerService {
    Customer createCustomer(Customer customer);
    Customer createCustomer(Customer customer, Account initialAccount);
    List<Customer> listCustomers();
}
