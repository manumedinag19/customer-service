package com.example.customerservice.adapters.inbound.rest.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import io.swagger.v3.oas.annotations.media.Schema;

public class CustomerRequest {

    @NotBlank(message = "fullName is required")
    @Schema(description = "Nombre completo del cliente", example = "Juan Pérez")
    private String fullName;

    @NotBlank(message = "documentType is required")
    @Schema(description = "Tipo de documento (CC, NIT, etc.)", example = "CC")
    private String documentType;

    @NotBlank(message = "documentNumber is required")
    @Schema(description = "Número de documento del cliente", example = "12345678")
    private String documentNumber;

    @NotBlank(message = "email is required")
    @Email(message = "email must be a valid email address")
    @Schema(description = "Correo electrónico del cliente", example = "juan.perez@example.com")
    private String email;

    public CustomerRequest() {
    }

    public CustomerRequest(String fullName, String documentType, String documentNumber, String email) {
        this.fullName = fullName;
        this.documentType = documentType;
        this.documentNumber = documentNumber;
        this.email = email;
    }

    // Constructor para compatibilidad: fullName, email (sin documentNumber/documentType)
    public CustomerRequest(String fullName, String email) {
        this.fullName = fullName;
        this.email = email;
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
}
