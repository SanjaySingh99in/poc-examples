package com.digital.demo.drool.example1.entity;


import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "payment_transactions")
@Data
public class PaymentTransaction {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotNull
    @Column(nullable = false)
    private String transactionReference;

    @NotNull
    @Column(nullable = false)
    private String customerId;

    @NotNull
    @Column(nullable = false)
    private String fromAccount;

    @NotNull
    @Column(nullable = false)
    private String toAccount;

    @NotNull
    @DecimalMin(value = "0.01", message = "Amount must be greater than 0")
    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal amount;

    @NotNull
    @Column(nullable = false)
    private String currency = "USD";

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentStatus status = PaymentStatus.PENDING;

    private String failureReason;

    @Column(nullable = false)
    private LocalDateTime transactionDate = LocalDateTime.now();

    private LocalDateTime processedDate;

    private String paymentMethod; // "BANK_TRANSFER", "CARD", "WALLET"

    private String transactionType; // "DOMESTIC", "INTERNATIONAL", "INTERNAL"

    private BigDecimal customerBalance; // Current balance after transaction
    private Integer customerTransactionCount; // Monthly transaction count
    private String customerRiskLevel; // "LOW", "MEDIUM", "HIGH"
    private Boolean isHighRiskCountry = false;
    private Boolean isSuspiciousAmount = false;
    private BigDecimal dailyTransactionTotal = BigDecimal.ZERO;
}
