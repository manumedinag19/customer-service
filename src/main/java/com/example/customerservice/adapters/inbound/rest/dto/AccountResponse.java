package com.example.customerservice.adapters.inbound.rest.dto;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;
import io.swagger.v3.oas.annotations.media.Schema;

public class AccountResponse {
    @Schema(description = "Identificador de la cuenta", example = "3fa85f64-5717-4562-b3fc-2c963f66afa6")
    private UUID id;
    @Schema(description = "Identificador del cliente", example = "3fa85f64-5717-4562-b3fc-2c963f66afa6")
    private UUID customerId;
    @Schema(description = "Número de cuenta", example = "ES7921000813610123456789")
    private String number;
    @Schema(description = "Saldo actual", example = "1500.00")
    private BigDecimal balance;
    @Schema(description = "Tipo de cuenta", example = "SAVINGS")
    private String type;
    @Schema(description = "Fecha de creación", example = "2023-01-01T12:00:00Z")
    private Instant createdAt;

    public AccountResponse() {
    }

    public AccountResponse(UUID id, UUID customerId, String number, BigDecimal balance, String type, Instant createdAt) {
        this.id = id;
        this.customerId = customerId;
        this.number = number;
        this.balance = balance;
        this.type = type;
        this.createdAt = createdAt;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getCustomerId() {
        return customerId;
    }

    public void setCustomerId(UUID customerId) {
        this.customerId = customerId;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }
}
