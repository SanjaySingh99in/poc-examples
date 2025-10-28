package com.digital.demo.drool.example1.entity;


import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "customers")
@Data
public class Customer {
    @Id
    private String customerId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CustomerType customerType = CustomerType.REGULAR; // REGULAR, PREMIUM, BUSINESS

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal accountBalance = BigDecimal.ZERO;

    private BigDecimal dailyTransactionLimit = new BigDecimal("5000.00");
    private BigDecimal monthlyTransactionLimit = new BigDecimal("50000.00");

    @Column(nullable = false)
    private Integer monthlyTransactionCount = 0;

    @Column(precision = 15, scale = 2)
    private BigDecimal monthlyTransactionTotal = BigDecimal.ZERO;

    @Column(precision = 15, scale = 2)
    private BigDecimal dailyTransactionTotal = BigDecimal.ZERO;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AccountStatus accountStatus = AccountStatus.ACTIVE;

    private LocalDateTime lastTransactionDate;
    private String riskLevel = "LOW";
    private Boolean isAccountFrozen = false;

    public enum CustomerType {
        REGULAR, PREMIUM, BUSINESS, GOLD, PLATINUM
    }

    public enum AccountStatus {
        ACTIVE, INACTIVE, SUSPENDED, FROZEN, CLOSED
    }
}
