package com.digital.demo.drool.example1.config;



import com.digital.demo.drool.example1.entity.Customer;
import com.digital.demo.drool.example1.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final CustomerRepository customerRepository;

    @Override
    public void run(String... args) throws Exception {
        // Create sample customers
        Customer customer1 = new Customer();
        customer1.setCustomerId("CUST001");
        customer1.setName("John Doe");
        customer1.setEmail("john.doe@email.com");
        customer1.setAccountBalance(new BigDecimal("10.00"));
        customer1.setRiskLevel("LOW");

        Customer customer2 = new Customer();
        customer2.setCustomerId("CUST002");
        customer2.setName("Jane Smith");
        customer2.setEmail("jane.smith@email.com");
        customer2.setAccountBalance(new BigDecimal("5000.00"));
        customer2.setRiskLevel("MEDIUM");

        Customer customer3 = new Customer();
        customer3.setCustomerId("CUST003");
        customer3.setName("High Risk Customer");
        customer3.setEmail("risk.customer@email.com");
        customer3.setAccountBalance(new BigDecimal("2000.00"));
        customer3.setRiskLevel("HIGH");

        customerRepository.save(customer1);
        customerRepository.save(customer2);
        customerRepository.save(customer3);

        System.out.println("Sample data initialized successfully");
    }
}
