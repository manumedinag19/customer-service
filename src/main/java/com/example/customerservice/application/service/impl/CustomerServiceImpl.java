package com.example.customerservice.application.service.impl;

import com.example.customerservice.application.service.CustomerService;
    import com.example.customerservice.application.service.AccountService;
import com.example.customerservice.domain.model.Customer;
import com.example.customerservice.domain.model.Account;
import com.example.customerservice.domain.port.outbound.CustomerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CustomerServiceImpl implements CustomerService {

    private static final Logger log = LoggerFactory.getLogger(CustomerServiceImpl.class);

    private final CustomerRepository customerRepository;
    private final AccountService accountService;

    public CustomerServiceImpl(CustomerRepository customerRepository, AccountService accountService) {
        this.customerRepository = customerRepository;
        this.accountService = accountService;
    }

    @Override
    public Customer createCustomer(Customer customer) {
        log.info("Creando cliente: nombre='{}', email='{}'", customer.getName(), customer.getEmail());
        Customer saved = customerRepository.save(customer);
        log.info("Cliente guardado con id='{}'", saved.getId());
        return saved;
    }

    @Override
    @Transactional
    public Customer createCustomer(Customer customer, Account initialAccount) {
        log.info("Creando cliente con cuenta opcional: nombre='{}', email='{}'", customer.getName(), customer.getEmail());
        Customer saved = customerRepository.save(customer);
        log.info("Cliente guardado con id='{}'", saved.getId());
        if (initialAccount != null) {
            initialAccount.setCustomerId(saved.getId());
            log.info("Creando cuenta inicial para clienteId='{}'", saved.getId());
            accountService.createAccount(initialAccount);
        }
        return saved;
    }

    @Override
    public List<Customer> listCustomers() {
        log.info("Obteniendo lista de clientes");
        List<Customer> list = customerRepository.findAll();
        log.info("Se encontraron {} clientes", list.size());
        return list;
    }
}
