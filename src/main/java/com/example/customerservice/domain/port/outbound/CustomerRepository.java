package com.example.customerservice.domain.port.outbound;

import com.example.customerservice.domain.model.Customer;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CustomerRepository {
    Customer save(Customer customer);
    List<Customer> findAll();
    Optional<Customer> findById(UUID id);
    boolean existsById(UUID id);
    Optional<Customer> findByDocumentNumber(String documentNumber);
}
