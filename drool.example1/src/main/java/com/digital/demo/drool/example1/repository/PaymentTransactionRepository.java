package com.digital.demo.drool.example1.repository;


import com.digital.demo.drool.example1.entity.PaymentTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface PaymentTransactionRepository extends JpaRepository<PaymentTransaction, UUID> {

    List<PaymentTransaction> findByCustomerId(String customerId);
    List<PaymentTransaction> findByStatus(String status);

    @Query("SELECT SUM(p.amount) FROM PaymentTransaction p WHERE p.customerId = :customerId AND p.transactionDate >= :startDate")
    Double findDailyTransactionTotal(@Param("customerId") String customerId, @Param("startDate") LocalDateTime startDate);

    @Query("SELECT COUNT(p) FROM PaymentTransaction p WHERE p.customerId = :customerId AND p.transactionDate >= :startDate")
    Long findMonthlyTransactionCount(@Param("customerId") String customerId, @Param("startDate") LocalDateTime startDate);
}
