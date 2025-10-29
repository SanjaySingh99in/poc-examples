package com.digital.demo.drool.example1.model;


import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class PaymentRequestDTO {
    @NotNull(message = "Customer ID is required")
    private String customerId;

//    @NotNull(message = "From account is required")
    private String fromAccount;

//    @NotNull(message = "To account is required")
    private String toAccount;

    @NotNull(message = "Amount is required")
    @DecimalMin(value = "0.01", message = "Amount must be greater than 0")
    private BigDecimal amount;

    private String currency = "USD";
    private String paymentMethod = "BANK_TRANSFER";
    private String transactionType = "DOMESTIC";
    private String description;
}
