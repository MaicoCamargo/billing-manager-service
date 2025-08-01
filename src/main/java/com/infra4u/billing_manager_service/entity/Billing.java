package com.infra4u.billing_manager_service.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import java.util.UUID;

@Entity
@NoArgsConstructor
public class Billing {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    @Setter
    private Long id;

    @Getter
    @Setter
    @Column(length = 2500)
    private String name;

    @Getter
    @Setter
    @Column(length = 5000)
    private String description;

    @ManyToOne
    @JoinColumn(name = "type_id")
    private BillingType billingType;

    @Getter
    @Setter
    private BigDecimal amount;

    @Column(name = "order_id")
    @Getter
    private Long orderId;

    @Column(name = "installment_count")
    @Setter
    private Integer installmentCount;

    @Column(name = "service_id")
    private Long serviceId;

    @Column(name = "created_at")
    @Getter
    private LocalDateTime createdAt;

    @Column(name = "user_id")
    @Getter
    @Setter
    private Long userId;

    @OneToMany(mappedBy = "billing")
    private List<BillingStatus> billingStatuses;

    @OneToMany(mappedBy = "billing", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    @Getter
    @Setter
    private List<Payment> payments;

    @Column(name = "code", unique = true, length = 6)
    @Getter
    @Setter
    private String code;

    public Billing(String name, String description, BigDecimal amount, Integer installmentCount, Long orderId, Long serviceId, Long userId, List<Payment> payments) {
        this.name = name;
        this.description = description;
        this.amount = amount;
        this.orderId = orderId;
        this.installmentCount = installmentCount;
        this.serviceId = serviceId;
        this.userId = userId;
        this.createdAt = LocalDateTime.now();
        payments.forEach(payment -> payment.setBilling(this));
        this.payments = payments;
        this.code = generateUniqueCode();
    }

    private String generateUniqueCode() {
        StringBuilder codeBuilder = new StringBuilder("B");
        Random random = new Random();
        String characters = "ABCDEFGHJKMNPQRSTUVWXYZ23456789";
        for (int i = 0; i < 5; i++) {
            int index = random.nextInt(characters.length());
            codeBuilder.append(characters.charAt(index));
        }
        return codeBuilder.toString();
    }
}
