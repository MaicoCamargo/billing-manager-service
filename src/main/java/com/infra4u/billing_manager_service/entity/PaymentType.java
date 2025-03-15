package com.infra4u.billing_manager_service.entity;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class PaymentType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @OneToMany(mappedBy = "paymentType")
    private List<Payment> payments;

    // Getters and Setters
}
