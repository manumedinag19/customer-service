package com.example.customerservice.unit;

import com.example.customerservice.application.service.impl.CustomerServiceImpl;
import com.example.customerservice.domain.model.Customer;
import com.example.customerservice.domain.port.outbound.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class CustomerServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private CustomerServiceImpl customerService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createCustomer_shouldReturnSavedCustomer() {
        Customer toSave = new Customer(null, "Test", "test@example.com", null);
        Customer saved = new Customer(UUID.randomUUID(), "Test", "test@example.com", Instant.now());

        when(customerRepository.save(any(Customer.class))).thenReturn(saved);

        Customer result = customerService.createCustomer(toSave);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(saved.getId());
        assertThat(result.getEmail()).isEqualTo("test@example.com");
    }

    @Test
    void listCustomers_shouldReturnAll() {
        Customer c1 = new Customer(UUID.randomUUID(), "A", "a@example.com", Instant.now());
        Customer c2 = new Customer(UUID.randomUUID(), "B", "b@example.com", Instant.now());

        when(customerRepository.findAll()).thenReturn(List.of(c1, c2));

        var list = customerService.listCustomers();

        assertThat(list).hasSize(2);
        assertThat(list).contains(c1, c2);
    }
}

