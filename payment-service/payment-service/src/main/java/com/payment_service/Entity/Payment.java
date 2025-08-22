package com.payment_service.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Payment {
   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Integer id;
   private String phoneNumber;
   private double amount;

   private String merchantRequestId;
   private String checkoutRequestId;

   private int resultCode;
   private String resultDescription;
   private String mpesaReceiptNumber;
   private String status;
   private LocalDateTime localDateTime;


}
