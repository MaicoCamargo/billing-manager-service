package com.infra4u.billing_manager_service.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
class CreateSimpleBillingIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("Verify create billing with POST /billings/simple using nested structure")
    void verifyCreateSimpleBilling() throws Exception {
        String clientId = "test-client-id";
        String payload = """
                {
                  "name": "Simple Billing",
                  "description": "Simple Description",
                  "order": "3859a0c4-6000-485f-a757-afa53c8abf21",
                  "amount": 250,
                  "payments": [
                    {
                      "type": "CREDIT_CARD",
                      "value": 150,
                      "installments": 8
                    },
                    {
                      "type": "CASH_ON_DELIVERY",
                      "value": 100,
                      "installments": 1
                    }
                  ]
                }
                """;

        mockMvc.perform(post("/billings/simple")
                .header("X-Client-ID", clientId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(payload)
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.content.name").value("Simple Billing"))
            .andExpect(jsonPath("$.content.description").value("Simple Description"))
            .andExpect(jsonPath("$.content.amount").value(250))
            .andExpect(jsonPath("$.content.order").value("3859a0c4-6000-485f-a757-afa53c8abf21"))
            .andExpect(jsonPath("$.content.status").value("IN_PROGRESS"))
            .andExpect(jsonPath("$.content.code").exists())
            .andExpect(jsonPath("$.content.payments").isArray())
            .andExpect(jsonPath("$.content.payments.length()").value(2))
            // Validate individual payments
            .andExpect(jsonPath("$.content.payments[?(@.type.name == 'CREDIT_CARD')].value").value(150))
            .andExpect(jsonPath("$.content.payments[?(@.type.name == 'CREDIT_CARD')].installment").value(8))
            .andExpect(jsonPath("$.content.payments[?(@.type.name == 'CREDIT_CARD')].type.name").value("CREDIT_CARD"))
            .andExpect(jsonPath("$.content.payments[?(@.type.name == 'CREDIT_CARD')].type.id").value(3))
            .andExpect(jsonPath("$.content.payments[?(@.type.name == 'CREDIT_CARD')].expirationDate").exists())
            .andExpect(jsonPath("$.content.payments[?(@.type.name == 'CREDIT_CARD')].status.name").value("PENDING"))
            .andExpect(jsonPath("$.content.payments[?(@.type.name == 'CREDIT_CARD')].status.id").value(1))
            .andExpect(jsonPath("$.content.payments[?(@.type.name == 'CREDIT_CARD')].status.date").exists())
            
            .andExpect(jsonPath("$.content.payments[?(@.type.name == 'CASH_ON_DELIVERY')].value").value(100))
            .andExpect(jsonPath("$.content.payments[?(@.type.name == 'CASH_ON_DELIVERY')].installment").value(1))
            .andExpect(jsonPath("$.content.payments[?(@.type.name == 'CASH_ON_DELIVERY')].type.name").value("CASH_ON_DELIVERY"))
            .andExpect(jsonPath("$.content.payments[?(@.type.name == 'CASH_ON_DELIVERY')].type.id").value(1))
            .andExpect(jsonPath("$.content.payments[?(@.type.name == 'CASH_ON_DELIVERY')].expirationDate").exists())
            .andExpect(jsonPath("$.content.payments[?(@.type.name == 'CASH_ON_DELIVERY')].status.name").value("COMPLETED"))
            .andExpect(jsonPath("$.content.payments[?(@.type.name == 'CASH_ON_DELIVERY')].status.id").value(2))
            .andExpect(jsonPath("$.content.payments[?(@.type.name == 'CASH_ON_DELIVERY')].status.date").exists())
            
            .andExpect(jsonPath("$.metadata.message").value("Request processed successfully"))
            .andExpect(jsonPath("$.metadata.timestamp").exists())
            .andExpect(jsonPath("$.pagination").doesNotExist())
            .andDo(result -> {
                System.out.println("Response body: " + result.getResponse().getContentAsString());
            });
    }

}
