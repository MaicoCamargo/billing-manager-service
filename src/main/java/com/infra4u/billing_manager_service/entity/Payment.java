package com.infra4u.billing_manager_service.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private BigDecimal value;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "billing_id")
    private Billing billing;

    private Integer installment;

    @ManyToOne
    @JoinColumn(name = "type_id")
    private PaymentType paymentType;

    @Column(name = "expiration_date")
    private LocalDateTime expirationDate;

    @OneToMany(mappedBy = "payment")
    private List<PaymentStatus> paymentStatuses;

    // Getters and Setters
}
