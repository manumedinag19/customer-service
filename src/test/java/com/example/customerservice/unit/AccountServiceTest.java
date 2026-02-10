package com.example.customerservice.unit;

import com.example.customerservice.application.service.impl.AccountServiceImpl;
import com.example.customerservice.domain.model.Account;
import com.example.customerservice.domain.port.outbound.AccountRepository;
import com.example.customerservice.domain.port.outbound.CustomerRepository;
import com.example.customerservice.shared.exception.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class AccountServiceTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private AccountServiceImpl accountService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createAccount_customerNotFound_shouldThrow() {
        UUID cid = UUID.randomUUID();
        when(customerRepository.existsById(cid)).thenReturn(false);

        Account a = new Account(null, cid, "ACC-1", BigDecimal.ZERO, "SAVINGS", null);

        assertThatThrownBy(() -> accountService.createAccount(a)).isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void listAccounts_byCustomer_returnsList() {
        UUID cid = UUID.randomUUID();
        Account a1 = new Account(UUID.randomUUID(), cid, "ACC-1", BigDecimal.ZERO, "SAVINGS", Instant.now());
        when(accountRepository.findAllByCustomerId(cid)).thenReturn(List.of(a1));

        var list = accountService.listAccounts(cid);

        assertThat(list).hasSize(1);
        assertThat(list.get(0).getCustomerId()).isEqualTo(cid);
    }
}

