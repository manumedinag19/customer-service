package com.example.customerservice.adapters.inbound.rest.controller;

import com.example.customerservice.adapters.inbound.rest.dto.CustomerRequest;
import com.example.customerservice.adapters.inbound.rest.dto.CustomerResponse;
import com.example.customerservice.adapters.inbound.rest.mapper.RestMapper;
import com.example.customerservice.application.service.CustomerService;
import com.example.customerservice.domain.model.Customer;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/customers")
@Tag(name = "Customers", description = "Operaciones sobre clientes")
public class CustomerController {

    private static final Logger log = LoggerFactory.getLogger(CustomerController.class);

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @Operation(summary = "Crear un cliente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Cliente creado", content = @Content(schema = @Schema(implementation = CustomerResponse.class))),
            @ApiResponse(responseCode = "400", description = "Entrada inválida", content = @Content)
    })
    @PostMapping
    public ResponseEntity<CustomerResponse> createCustomer(@Valid @RequestBody CustomerRequest request) {
        log.info("Solicitud POST /api/customers - nombre={}, email={}", request.getFullName(), request.getEmail());
        Customer customer = RestMapper.toDomain(request);
        Customer saved = customerService.createCustomer(customer);
        CustomerResponse resp = RestMapper.toCustomerResponse(saved);
        log.info("Cliente creado - id={}", resp.getId());
        return ResponseEntity.created(URI.create("/api/customers/" + resp.getId())).body(resp);
    }

    @Operation(summary = "Listar clientes")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de clientes")
    })
    @GetMapping
    public ResponseEntity<List<CustomerResponse>> listCustomers() {
        log.info("Solicitud GET /api/customers");
        List<CustomerResponse> list = customerService.listCustomers().stream().map(RestMapper::toCustomerResponse).collect(Collectors.toList());
        log.info("Devolviendo {} clientes", list.size());
        return ResponseEntity.ok(list);
    }
}
