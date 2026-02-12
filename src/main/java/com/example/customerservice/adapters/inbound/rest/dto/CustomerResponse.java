package com.example.customerservice.adapters.inbound.rest.dto;

import java.time.Instant;
import java.util.UUID;
import io.swagger.v3.oas.annotations.media.Schema;

public class CustomerResponse {
    @Schema(description = "Identificador del cliente", example = "3fa85f64-5717-4562-b3fc-2c963f66afa6")
    private UUID id;
    @Schema(description = "Nombre completo del cliente", example = "Juan Pérez")
    private String fullName;
    @Schema(description = "Tipo de documento", example = "CC")
    private String documentType;
    @Schema(description = "Número de documento del cliente", example = "12345678")
    private String documentNumber;
    @Schema(description = "Correo electrónico del cliente", example = "juan.perez@example.com")
    private String email;
    @Schema(description = "Fecha de creación", example = "2023-01-01T12:00:00Z")
    private Instant createdAt;

    public CustomerResponse() {
    }

    public CustomerResponse(UUID id, String fullName, String documentType, String documentNumber, String email, Instant createdAt) {
        this.id = id;
        this.fullName = fullName;
        this.documentType = documentType;
        this.documentNumber = documentNumber;
        this.email = email;
        this.createdAt = createdAt;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getDocumentType() {
        return documentType;
    }

    public void setDocumentType(String documentType) {
        this.documentType = documentType;
    }

    public String getDocumentNumber() {
        return documentNumber;
    }

    public void setDocumentNumber(String documentNumber) {
        this.documentNumber = documentNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }
}
