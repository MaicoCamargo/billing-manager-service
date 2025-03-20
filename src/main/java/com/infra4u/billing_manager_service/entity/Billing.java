package com.infra4u.billing_manager_service.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@NoArgsConstructor
public class Billing {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    private Long id;

    @Getter
    private String name;
    @Getter
    private String description;

    @ManyToOne
    @JoinColumn(name = "type_id")
    private BillingType billingType;

    @Getter
    private BigDecimal amount;

    @Column(name = "order_id")
    private Long orderId;

    @Column(name = "installment_count")
    private Integer installmentCount;

    @Column(name = "service_id")
    private Long serviceId;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "user_id")
    private Long userId;

    @OneToMany(mappedBy = "billing")
    private List<BillingStatus> billingStatuses;

    @OneToMany(mappedBy = "billing", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @Getter
    private List<Payment> payments;

    public Billing(String name, String description, BigDecimal amount, Integer installmentCount, Long orderId, List<Payment> payments) {
        this.name = name;
        this.description = description;
        this.amount = amount;
        this.orderId = orderId;
        this.installmentCount = installmentCount;
        this.serviceId = 0L;
        this.userId = 0L;
        this.createdAt = LocalDateTime.now();
        payments.forEach(payment -> payment.setBilling(this));
        this.payments = payments;
    }
}
