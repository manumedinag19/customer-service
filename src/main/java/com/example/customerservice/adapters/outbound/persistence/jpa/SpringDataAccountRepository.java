package com.example.customerservice.adapters.outbound.persistence.jpa;

import com.example.customerservice.adapters.outbound.persistence.entity.AccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface SpringDataAccountRepository extends JpaRepository<AccountEntity, UUID> {
    List<AccountEntity> findAllByCustomerId(UUID customerId);
}

