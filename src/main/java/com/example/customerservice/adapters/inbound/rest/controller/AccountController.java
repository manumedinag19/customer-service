package com.example.customerservice.adapters.inbound.rest.controller;

import com.example.customerservice.adapters.inbound.rest.dto.AccountRequest;
import com.example.customerservice.adapters.inbound.rest.dto.AccountResponse;
import com.example.customerservice.adapters.inbound.rest.mapper.RestMapper;
import com.example.customerservice.application.service.AccountService;
import com.example.customerservice.domain.model.Account;
import com.example.customerservice.domain.model.Customer;
import com.example.customerservice.domain.port.outbound.CustomerRepository;
import com.example.customerservice.shared.exception.ResourceNotFoundException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/accounts")
@Tag(name = "Accounts", description = "Operaciones sobre cuentas")
public class AccountController {

    private static final Logger log = LoggerFactory.getLogger(AccountController.class);

    private final AccountService accountService;
    private final CustomerRepository customerRepository;

    public AccountController(AccountService accountService, CustomerRepository customerRepository) {
        this.accountService = accountService;
        this.customerRepository = customerRepository;
    }

    @Operation(summary = "Crear una cuenta")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Cuenta creada", content = @Content(schema = @Schema(implementation = AccountResponse.class))),
            @ApiResponse(responseCode = "400", description = "Entrada inválida o violación de constraints", content = @Content),
            @ApiResponse(responseCode = "404", description = "Cliente no encontrado", content = @Content)
    })
    @PostMapping
    public ResponseEntity<AccountResponse> createAccount(@Valid @RequestBody AccountRequest request) {
        log.info("Solicitud POST /api/accounts - customerId={}, número={}, saldoInicial={}", request.getCustomerId(), request.getNumber(), request.getInitialBalance());
        Account account = RestMapper.toDomain(request);
        Account saved = accountService.createAccount(account);
        AccountResponse resp = RestMapper.toAccountResponse(saved);
        log.info("Cuenta creada - id={}", resp.getId());
        return ResponseEntity.created(URI.create("/api/accounts/" + resp.getId())).body(resp);
    }

    @Operation(summary = "Listar cuentas (filtrar por identificacion del cliente - documento o UUID)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de cuentas retornada"),
            @ApiResponse(responseCode = "400", description = "identificacion inválida", content = @Content),
            @ApiResponse(responseCode = "404", description = "Cliente no encontrado", content = @Content)
    })
    @GetMapping
    public ResponseEntity<List<AccountResponse>> listAccounts(@RequestParam(required = false, name = "identificacion") String identificacion) {
        log.info("Solicitud GET /api/accounts - identificacion={}", identificacion);

        // Si no se pasa identificacion, devolver todas las cuentas
        if (identificacion == null || identificacion.isBlank()) {
            List<AccountResponse> all = accountService.listAccounts(null).stream().map(RestMapper::toAccountResponse).collect(Collectors.toList());
            log.info("Devolviendo {} cuentas (sin filtro)", all.size());
            return ResponseEntity.ok(all);
        }

        // Priorizar búsqueda por número de documento
        Optional<Customer> byDocument = customerRepository.findByDocumentNumber(identificacion);
        UUID resolvedCustomerId;
        if (byDocument.isPresent()) {
            resolvedCustomerId = byDocument.get().getId();
        } else {
            // interpretar como UUID y buscar por id
            UUID id;
            try {
                id = UUID.fromString(identificacion);
            } catch (IllegalArgumentException ex) {
                throw new ResourceNotFoundException("No se encontró cliente con identificación: " + identificacion);
            }
            Optional<Customer> byId = customerRepository.findById(id);
            if (byId.isPresent()) {
                resolvedCustomerId = id;
            } else {
                throw new ResourceNotFoundException("No se encontró cliente con identificación: " + identificacion);
            }
        }

        List<AccountResponse> list = accountService.listAccounts(resolvedCustomerId).stream().map(RestMapper::toAccountResponse).collect(Collectors.toList());
        log.info("Devolviendo {} cuentas", list.size());
        return ResponseEntity.ok(list);
    }
}
