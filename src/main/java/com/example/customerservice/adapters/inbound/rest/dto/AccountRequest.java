package com.example.customerservice.adapters.inbound.rest.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;
import java.util.UUID;
import io.swagger.v3.oas.annotations.media.Schema;

public class AccountRequest {

    @NotNull(message = "customerId is required")
    @Schema(description = "ID del cliente dueño de la cuenta", example = "3fa85f64-5717-4562-b3fc-2c963f66afa6")
    private UUID customerId;

    @NotBlank(message = "number is required")
    @Schema(description = "Número de cuenta", example = "ES7921000813610123456789")
    private String number;

    @NotNull(message = "initialBalance is required")
    @PositiveOrZero(message = "initialBalance must be zero or positive")
    @Schema(description = "Saldo inicial", example = "1000.00")
    private BigDecimal initialBalance;

    @NotBlank(message = "type is required")
    @Schema(description = "Tipo de cuenta", example = "AHORRO")
    private String type;

    public AccountRequest() {
    }

    public AccountRequest(UUID customerId, String number, BigDecimal initialBalance, String type) {
        this.customerId = customerId;
        this.number = number;
        this.initialBalance = initialBalance;
        this.type = type;
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

    public BigDecimal getInitialBalance() {
        return initialBalance;
    }

    public void setInitialBalance(BigDecimal initialBalance) {
        this.initialBalance = initialBalance;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
