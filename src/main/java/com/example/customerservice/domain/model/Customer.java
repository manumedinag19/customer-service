package com.example.customerservice.domain.model;

import java.time.Instant;
import java.util.UUID;

public class Customer {
    private UUID id;
    private String fullName;
    private String email;
    private String documentType;
    private String documentNumber;
    private Instant createdAt;

    public Customer(UUID id, String fullName, String email, String documentType, String documentNumber, Instant createdAt) {
        this.id = id;
        this.fullName = fullName;
        this.email = email;
        this.documentType = documentType;
        this.documentNumber = documentNumber;
        this.createdAt = createdAt;
    }

    // Constructor para compatibilidad (sin documentNumber/documentType)
    public Customer(UUID id, String fullName, String email, Instant createdAt) {
        this(id, fullName, email, null, null, createdAt);
    }

    // Constructor para crear sin id ni createdAt
    public Customer(String fullName, String email, String documentType, String documentNumber) {
        this(null, fullName, email, documentType, documentNumber, null);
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    // compatibilidad: getName delega a fullName
    public String getName() {
        return fullName;
    }

    public void setName(String name) {
        this.fullName = name;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }
}
