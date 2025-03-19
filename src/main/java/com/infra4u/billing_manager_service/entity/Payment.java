package com.infra4u.billing_manager_service.entity;

import com.infra4u.billing_manager_service.use_case.payment_status.Statuses;
import jakarta.persistence.*;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Entity
@NoArgsConstructor
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private BigDecimal value;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "billing_id")
    @Setter
    private Billing billing;

    private Integer installment;

    @ManyToOne
    @JoinColumn(name = "type_id")
    private PaymentType paymentType;

    @Column(name = "expiration_date")
    private LocalDateTime expirationDate;

    @OneToMany(mappedBy = "payment", cascade = CascadeType.ALL)
    private List<PaymentStatus> paymentStatuses;

    public Payment(BigDecimal value, Integer installment, PaymentType paymentType, LocalDateTime expirationDate, List<PaymentStatus> statuses) {
        this.value = value;
        this.installment = installment;
        this.paymentType = paymentType;
        this.expirationDate = expirationDate;
        this.createdAt = LocalDateTime.now();
        statuses.forEach(stat -> stat.setPayment(this));
        this.paymentStatuses = statuses;
    }

    public Payment(BigDecimal value, Integer installment, PaymentType paymentType, LocalDateTime expirationDate) {
        this.value = value;
        this.installment = installment;
        this.paymentType = paymentType;
        this.expirationDate = expirationDate;
        this.createdAt = LocalDateTime.now();
        PaymentStatus status = new PaymentStatus(new Status(Statuses.PENDING.getStatus()));
        this.paymentStatuses = Collections.singletonList(status);
    }
}
