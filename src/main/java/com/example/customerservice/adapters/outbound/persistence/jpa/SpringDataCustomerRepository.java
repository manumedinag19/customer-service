package com.example.customerservice.adapters.outbound.persistence.jpa;

import com.example.customerservice.adapters.outbound.persistence.entity.CustomerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface SpringDataCustomerRepository extends JpaRepository<CustomerEntity, UUID> {
    Optional<CustomerEntity> findByDocumentNumber(String documentNumber);
}
