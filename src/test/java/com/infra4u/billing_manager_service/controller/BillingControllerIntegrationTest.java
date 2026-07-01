package com.infra4u.billing_manager_service.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class BillingControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("Verify load billings by order ID with X-Client-ID header")
    void shouldLoadBillingByOrderWithClientId() throws Exception {
        String clientId = "test-client-id";

        // Arrange (Create)
        String newOrderId = java.util.UUID.randomUUID().toString();
        String payload = """
                {
                  "name": "Integration Test Billing",
                  "description": "Created after load",
                  "amount": 300,
                  "order": "%s",
                  "futurePayments": [
                    {
                      "value": 300,
                      "installments": 2,
                      "type": "CREDIT_CARD",
                      "expiresAt": "2026-10-28"
                    }
                  ]
                }
                """.formatted(newOrderId);

        // Act & Assert (Create)
        mockMvc.perform(post("/billings")
                .header("X-Client-ID", clientId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(payload)
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.content.name").value("Integration Test Billing"))
            .andExpect(jsonPath("$.content.order").value(newOrderId))
            .andExpect(jsonPath("$.content.amount").value(300))
            .andExpect(jsonPath("$.content.payments.length()").value(1))
            .andExpect(jsonPath("$.content.payments[0].installment").value(2))
            .andExpect(jsonPath("$.content.payments[0].value").value(300))
            .andExpect(jsonPath("$.content.payments[0].status.name").value("IN_PROGRESS"))
            .andExpect(jsonPath("$.content.payments[0].status.id").value(7))
            .andDo(result -> {
                System.out.println("Create Response body: " + result.getResponse().getContentAsString());
            });
    }
}
