package com.Api.Helpers;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResponseWrapper<T> {
   private String status;
   private String message;
   private T data;
}
