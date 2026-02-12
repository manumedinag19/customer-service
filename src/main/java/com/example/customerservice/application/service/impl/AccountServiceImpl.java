package com.example.customerservice.application.service.impl;

import com.example.customerservice.application.service.AccountService;
import com.example.customerservice.domain.model.Account;
import com.example.customerservice.domain.port.outbound.AccountRepository;
import com.example.customerservice.domain.port.outbound.CustomerRepository;
import com.example.customerservice.shared.exception.ResourceNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class AccountServiceImpl implements AccountService {

    private static final Logger log = LoggerFactory.getLogger(AccountServiceImpl.class);

    private final AccountRepository accountRepository;
    private final CustomerRepository customerRepository;

    public AccountServiceImpl(AccountRepository accountRepository, CustomerRepository customerRepository) {
        this.accountRepository = accountRepository;
        this.customerRepository = customerRepository;
    }

    @Override
    public Account createAccount(Account account) {
        UUID customerId = account.getCustomerId();
        log.info("Creando cuenta para clienteId='{}'", customerId);
        if (!customerRepository.existsById(customerId)) {
            log.warn("No existe cliente con id='{}'", customerId);
            throw new ResourceNotFoundException("Cliente no encontrado con id: " + customerId);
        }
        if (account.getNumber() == null || account.getNumber().isBlank()) {
            String generated = generateAccountNumber();
            account.setNumber(generated);
            log.info("Número de cuenta generado automáticamente: {}", generated);
        }
        if (account.getBalance() == null) {
            account.setBalance(java.math.BigDecimal.ZERO);
        }
        Account saved = accountRepository.save(account);
        log.info("Cuenta guardada con id='{}'", saved.getId());
        return saved;
    }

    @Override
    public List<Account> listAccounts(UUID customerId) {
        log.info("Obteniendo cuentas para clienteId='{}'", customerId);
        List<Account> result;
        if (customerId == null) {
            result = accountRepository.findAll();
        } else {
            result = accountRepository.findAllByCustomerId(customerId);
        }
        log.info("Se encontraron {} cuentas", result.size());
        return result;
    }

    private String generateAccountNumber() {
        return "ACCT-" + UUID.randomUUID().toString().replace("-", "").substring(0, 10).toUpperCase();
    }
}
