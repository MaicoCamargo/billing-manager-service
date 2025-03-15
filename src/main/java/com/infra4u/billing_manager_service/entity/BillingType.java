package com.infra4u.billing_manager_service.entity;

import jakarta.persistence.*;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@NoArgsConstructor
public class BillingType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @OneToMany(mappedBy = "billingType")
    private List<Billing> billings;

    public BillingType(Long id) {
        this.id = id;
    }
}
