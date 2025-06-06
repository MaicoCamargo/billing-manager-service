package com.infra4u.billing_manager_service.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
public class PaymentStatus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "payment_id")
    @Setter
    private Payment payment;

    @ManyToOne
    @JoinColumn(name = "status_id")
    @Setter
    @Getter
    private Status status;

    @Column(name = "created_at")
    @Getter
    private LocalDateTime createdAt;

    public PaymentStatus(Status status) {
        this.status = status;
        this.createdAt = LocalDateTime.now();
    }

    public PaymentStatus(Status status, Payment payment) {
        this.status = status;
        this.createdAt = LocalDateTime.now();
        this.payment = payment;
    }
}