package com.payment_service.Dao;

import com.payment_service.Entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface PaymentRepo extends JpaRepository<Payment,Integer> {

    @Query("from Payment where checkoutRequestId=:checkOutId")
    public Optional<Payment> findByCheckoutID(@Param("checkOutId") String checkOutId);
}
