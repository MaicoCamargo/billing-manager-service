package com.infra4u.billing_manager_service.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@NoArgsConstructor
public class Status {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    @Setter
    private Long id;

    @Setter
    @Getter
    private String name;

    @OneToMany(mappedBy = "status")
    private List<BillingStatus> billingStatuses;

    @OneToMany(mappedBy = "status")
    private List<PaymentStatus> paymentStatuses;

    public Status(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Status(Long id) {
        this.id = id;
    }
}
