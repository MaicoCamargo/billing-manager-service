package com.infra4u.billing_manager_service.entity;

import com.infra4u.billing_manager_service.use_case.payment_status.Statuses;
import jakarta.persistence.*;
import lombok.Getter;
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
    @Getter
    @Setter
    private Long id;

    @Getter
    @Setter
    private BigDecimal value;

    @Column(name = "created_at")
    @Getter
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "billing_id")
    @Setter
    private Billing billing;

    @Getter
    @Setter
    private Integer installment;

    @ManyToOne
    @JoinColumn(name = "type_id")
    @Getter
    @Setter
    private PaymentType paymentType;

    @Column(name = "expiration_date")
    @Getter
    @Setter
    private LocalDateTime expirationDate;

    @OneToMany(mappedBy = "payment", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @Getter
    @Setter
    private List<PaymentStatus> paymentStatuses;

    public Payment(Long billingId, BigDecimal value, Integer installment, PaymentType paymentType, LocalDateTime expirationDate, List<PaymentStatus> statuses) {
        this.value = value;
        this.installment = installment;
        this.paymentType = paymentType;
        this.expirationDate = expirationDate;
        this.createdAt = LocalDateTime.now();
        statuses.forEach(stat -> stat.setPayment(this));
        this.paymentStatuses = statuses;
        final Billing billing = new Billing();
        billing.setId(billingId);
        this.billing = billing;
    }

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
        PaymentStatus status = new PaymentStatus(new Status(Statuses.PENDING.getId(), Statuses.PENDING.name()));
        status.setPayment(this);
        this.paymentStatuses = Collections.singletonList(status);
    }
}
