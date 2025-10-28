package com.digital.demo.drool.example1.model;


import com.digital.demo.drool.example1.entity.PaymentStatus;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class PaymentResponseDTO {
    private UUID transactionId;
    private String transactionReference;
    private String customerId;
    private String fromAccount;
    private String toAccount;
    private BigDecimal amount;
    private String currency;
    private PaymentStatus status;
    private String failureReason;
    private LocalDateTime transactionDate;
    private LocalDateTime processedDate;
    private BigDecimal customerBalance;
    private String message;
}
