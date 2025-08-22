package com.payment_service.Dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StkRequest {
   private double amount;
   private String phone;
   private String description;
   private String accountRef;
}
