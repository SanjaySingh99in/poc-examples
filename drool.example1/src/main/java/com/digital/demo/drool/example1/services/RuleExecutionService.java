package com.digital.demo.drool.example1.services;



import com.digital.demo.drool.example1.entity.Customer;
import com.digital.demo.drool.example1.entity.PaymentTransaction;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.kie.api.runtime.KieSession;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class RuleExecutionService {

    private final KieSession kieSession;

    public PaymentTransaction executePaymentRules(PaymentTransaction payment, Customer customer) {
        try {
            // Insert facts into Drools session
            kieSession.insert(payment);
            kieSession.insert(customer);

            // Set global variables if needed
            kieSession.setGlobal("logger", log);

            // Execute all rules
            int firedRules = kieSession.fireAllRules();
            log.info("Executed {} rules for payment: {}", firedRules, payment.getTransactionReference());

            return payment;

        } finally {
            // Clean up the session
            kieSession.dispose();
        }
    }
}