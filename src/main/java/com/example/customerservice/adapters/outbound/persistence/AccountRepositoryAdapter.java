package com.example.customerservice.adapters.outbound.persistence;

import com.example.customerservice.adapters.outbound.persistence.entity.AccountEntity;
import com.example.customerservice.adapters.outbound.persistence.jpa.SpringDataAccountRepository;
import com.example.customerservice.adapters.outbound.persistence.mapper.PersistenceMapper;
import com.example.customerservice.domain.model.Account;
import com.example.customerservice.domain.port.outbound.AccountRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class AccountRepositoryAdapter implements AccountRepository {

    private static final Logger log = LoggerFactory.getLogger(AccountRepositoryAdapter.class);

    private final SpringDataAccountRepository springDataAccountRepository;

    public AccountRepositoryAdapter(SpringDataAccountRepository springDataAccountRepository) {
        this.springDataAccountRepository = springDataAccountRepository;
    }

    @Override
    public Account save(Account account) {
        log.info("Guardando cuenta: clienteId='{}', número='{}'", account.getCustomerId(), account.getNumber());
        AccountEntity entity = PersistenceMapper.toEntity(account);
        AccountEntity saved = springDataAccountRepository.save(entity);
        Account domain = PersistenceMapper.toDomain(saved);
        log.debug("Cuenta persistida con id='{}'", domain.getId());
        return domain;
    }

    @Override
    public List<Account> findAll() {
        log.info("Buscando todas las cuentas");
        List<Account> list = springDataAccountRepository.findAll()
                .stream()
                .map(PersistenceMapper::toDomain)
                .collect(Collectors.toList());
        log.debug("Se encontraron {} cuentas", list.size());
        return list;
    }

    @Override
    public List<Account> findAllByCustomerId(UUID customerId) {
        log.info("Buscando cuentas por clienteId='{}'", customerId);
        List<Account> list = springDataAccountRepository.findAllByCustomerId(customerId)
                .stream()
                .map(PersistenceMapper::toDomain)
                .collect(Collectors.toList());
        log.debug("Se encontraron {} cuentas para clienteId='{}'", list.size(), customerId);
        return list;
    }

    @Override
    public Optional<Account> findById(UUID id) {
        log.info("Buscando cuenta por id='{}'", id);
        Optional<Account> result = springDataAccountRepository.findById(id).map(PersistenceMapper::toDomain);
        if (result.isPresent()) {
            log.debug("Cuenta encontrada id='{}'", id);
        } else {
            log.debug("Cuenta no encontrada id='{}'", id);
        }
        return result;
    }
}
