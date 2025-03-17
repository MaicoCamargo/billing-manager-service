package com.infra4u.billing_manager_service.entity;

import jakarta.persistence.*;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@NoArgsConstructor
public class PaymentType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @OneToMany(mappedBy = "paymentType")
    private List<Payment> payments;

    public PaymentType(Long id) {
        this.id = id;
    }
}
