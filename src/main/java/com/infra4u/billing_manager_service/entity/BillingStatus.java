package com.infra4u.billing_manager_service.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class BillingStatus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "billing_id")
    private Billing billing;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "status_id")
    private Status status;

    // Getters and Setters
}
