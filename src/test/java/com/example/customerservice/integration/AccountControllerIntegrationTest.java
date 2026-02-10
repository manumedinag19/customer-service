package com.example.customerservice.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.example.customerservice.adapters.inbound.rest.dto.AccountRequest;
import com.example.customerservice.adapters.inbound.rest.dto.CustomerRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class AccountControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void createAccountCustomerNotFound() throws Exception {
        AccountRequest req = new AccountRequest(UUID.randomUUID(), "ACC-1", BigDecimal.valueOf(100), "SAVINGS");
        mockMvc.perform(post("/api/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").exists());
    }

    @Test
    void createAccountAndListByCustomer() throws Exception {
        // create customer
        CustomerRequest creq = new CustomerRequest("Ana", "ana@example.com");
        String cbody = mockMvc.perform(post("/api/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(creq)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        // read id from response
        UUID customerId = objectMapper.readTree(cbody).get("id").asText() != null ? UUID.fromString(objectMapper.readTree(cbody).get("id").asText()) : null;

        AccountRequest areq = new AccountRequest(customerId, "ACC-2", BigDecimal.ZERO, "CHECKING");
        mockMvc.perform(post("/api/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(areq)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.customerId").value(customerId.toString()));

        mockMvc.perform(get("/api/accounts").param("customerId", customerId.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].customerId").value(customerId.toString()));
    }
}

