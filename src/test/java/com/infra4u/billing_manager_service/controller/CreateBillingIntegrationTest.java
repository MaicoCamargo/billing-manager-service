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
class CreateBillingIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("Verify create billing with POST /billings")
    void verifyCreateBilling() throws Exception {
        String clientId = "test-client-id";
        String payload = """
                {
                  "name": "test",
                  "description": "test",
                  "amount": 250,
                  "order": "3859a0c4-6000-485f-a757-afa53c8abf21",
                  "futurePayments": [
                    {
                      "value": 250,
                      "installments": 3,
                      "type": "CREDIT_CARD",
                      "expiresAt": "2026-09-26"
                    }
                  ]
                }
                """;

        mockMvc.perform(post("/billings")
                .header("X-Client-ID", clientId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(payload)
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.content.name").value("test"))
            .andExpect(jsonPath("$.content.amount").value(250))
            .andExpect(jsonPath("$.content.order").value("3859a0c4-6000-485f-a757-afa53c8abf21"))
            .andExpect(jsonPath("$.content.payments").isArray())
            .andExpect(jsonPath("$.content.payments[0].value").value(250))
            .andExpect(jsonPath("$.content.payments[0].installment").value(3))
            .andDo(result -> {
                System.out.println("Response body: " + result.getResponse().getContentAsString());
            });
    }
}
