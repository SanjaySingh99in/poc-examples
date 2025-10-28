package com.digital.demo.drool.example1.services;


import com.digital.demo.drool.example1.entity.Customer;
import com.digital.demo.drool.example1.entity.PaymentStatus;
import com.digital.demo.drool.example1.entity.PaymentTransaction;
import com.digital.demo.drool.example1.model.PaymentRequestDTO;
import com.digital.demo.drool.example1.model.PaymentResponseDTO;
import com.digital.demo.drool.example1.repository.CustomerRepository;
import com.digital.demo.drool.example1.repository.PaymentTransactionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.kie.api.runtime.KieSession;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentService {

    private final PaymentTransactionRepository paymentRepository;
    private final CustomerRepository customerRepository;
    private final KieSession kieSession;
    private final RuleExecutionService ruleExecutionService;

    @Transactional
    public PaymentResponseDTO processPayment(PaymentRequestDTO paymentRequest) {
        try {
            // Validate customer exists
            Customer customer = customerRepository.findById(paymentRequest.getCustomerId())
                    .orElseThrow(() -> new RuntimeException("Customer not found"));


            if (customer.getIsAccountFrozen()) {
                throw new RuntimeException("Customer account is frozen");
            }

            // Create payment transaction
            PaymentTransaction payment = createPaymentTransaction(paymentRequest, customer);
            payment = paymentRepository.save(payment);

            // Execute Drools rules
            PaymentTransaction processedPayment = ruleExecutionService.executePaymentRules(payment, customer);
            processedPayment = paymentRepository.save(processedPayment);

            // Update customer balance if payment completed
            if (processedPayment.getStatus() == PaymentStatus.COMPLETED) {
                updateCustomerBalance(customer, processedPayment.getAmount());
            }

            return mapToResponseDTO(processedPayment, "Payment processed successfully");

        } catch (Exception e) {
            log.error("Error processing payment: {}", e.getMessage());
            return createErrorResponse(paymentRequest, e.getMessage());
        }
    }

    private PaymentTransaction createPaymentTransaction(PaymentRequestDTO request, Customer customer) {
        PaymentTransaction payment = new PaymentTransaction();
        payment.setTransactionReference(generateTransactionReference());
        payment.setCustomerId(request.getCustomerId());
        payment.setFromAccount(request.getFromAccount());
        payment.setToAccount(request.getToAccount());
        payment.setAmount(request.getAmount());
        payment.setCurrency(request.getCurrency());
        payment.setPaymentMethod(request.getPaymentMethod());
        payment.setTransactionType(request.getTransactionType());
        payment.setStatus(PaymentStatus.PENDING);
        payment.setTransactionDate(LocalDateTime.now());

        // Set additional data for rule evaluation
        payment.setCustomerBalance(customer.getAccountBalance());
        payment.setCustomerTransactionCount(customer.getMonthlyTransactionCount());
        payment.setCustomerRiskLevel(customer.getRiskLevel());
        payment.setDailyTransactionTotal(customer.getDailyTransactionTotal());

        // Simulate risk checks
        payment.setIsHighRiskCountry(isHighRiskCountry(request.getToAccount()));
        payment.setIsSuspiciousAmount(isSuspiciousAmountPattern(request.getAmount()));

        return payment;
    }

    private void updateCustomerBalance(Customer customer, BigDecimal amount) {
        customer.setAccountBalance(customer.getAccountBalance().subtract(amount));
        customer.setMonthlyTransactionCount(customer.getMonthlyTransactionCount() + 1);
        customer.setMonthlyTransactionTotal(customer.getMonthlyTransactionTotal().add(amount));
        customer.setDailyTransactionTotal(customer.getDailyTransactionTotal().add(amount));
        customer.setLastTransactionDate(LocalDateTime.now());
        customerRepository.save(customer);
    }

    private String generateTransactionReference() {
        return "TXN" + System.currentTimeMillis() + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    private boolean isHighRiskCountry(String accountNumber) {
        // Simulate high-risk country check
        return accountNumber.startsWith("99");
    }

    private boolean isSuspiciousAmountPattern(BigDecimal amount) {
        // Simulate suspicious amount pattern detection
        return amount.remainder(new BigDecimal("100")).compareTo(BigDecimal.ZERO) == 0 &&
                amount.compareTo(new BigDecimal("1000")) > 0;
    }

    private PaymentResponseDTO mapToResponseDTO(PaymentTransaction payment, String message) {
        PaymentResponseDTO response = new PaymentResponseDTO();
        response.setTransactionId(payment.getId());
        response.setTransactionReference(payment.getTransactionReference());
        response.setCustomerId(payment.getCustomerId());
        response.setFromAccount(payment.getFromAccount());
        response.setToAccount(payment.getToAccount());
        response.setAmount(payment.getAmount());
        response.setCurrency(payment.getCurrency());
        response.setStatus(payment.getStatus());
        response.setFailureReason(payment.getFailureReason());
        response.setTransactionDate(payment.getTransactionDate());
        response.setProcessedDate(payment.getProcessedDate());
        response.setCustomerBalance(payment.getCustomerBalance());
        response.setMessage(message);
        return response;
    }

    private PaymentResponseDTO createErrorResponse(PaymentRequestDTO request, String error) {
        PaymentResponseDTO response = new PaymentResponseDTO();
        response.setCustomerId(request.getCustomerId());
        response.setFromAccount(request.getFromAccount());
        response.setToAccount(request.getToAccount());
        response.setAmount(request.getAmount());
        response.setCurrency(request.getCurrency());
        response.setStatus(PaymentStatus.FAILED);
        response.setFailureReason(error);
        response.setMessage("Payment processing failed");
        return response;
    }
}
