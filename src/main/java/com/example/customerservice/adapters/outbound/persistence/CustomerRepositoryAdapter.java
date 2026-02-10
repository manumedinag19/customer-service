package com.example.customerservice.adapters.outbound.persistence;

import com.example.customerservice.adapters.outbound.persistence.entity.CustomerEntity;
import com.example.customerservice.adapters.outbound.persistence.jpa.SpringDataCustomerRepository;
import com.example.customerservice.adapters.outbound.persistence.mapper.PersistenceMapper;
import com.example.customerservice.domain.model.Customer;
import com.example.customerservice.domain.port.outbound.CustomerRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class CustomerRepositoryAdapter implements CustomerRepository {

    private final SpringDataCustomerRepository springDataCustomerRepository;

    public CustomerRepositoryAdapter(SpringDataCustomerRepository springDataCustomerRepository) {
        this.springDataCustomerRepository = springDataCustomerRepository;
    }

    @Override
    public Customer save(Customer customer) {
        CustomerEntity entity = PersistenceMapper.toEntity(customer);
        CustomerEntity saved = springDataCustomerRepository.save(entity);
        return PersistenceMapper.toDomain(saved);
    }

    @Override
    public List<Customer> findAll() {
        return springDataCustomerRepository.findAll()
                .stream()
                .map(PersistenceMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Customer> findById(UUID id) {
        return springDataCustomerRepository.findById(id).map(PersistenceMapper::toDomain);
    }

    @Override
    public boolean existsById(UUID id) {
        return springDataCustomerRepository.existsById(id);
    }

    @Override
    public Optional<Customer> findByDocumentNumber(String documentNumber) {
        return springDataCustomerRepository.findByDocumentNumber(documentNumber).map(PersistenceMapper::toDomain);
    }
}
