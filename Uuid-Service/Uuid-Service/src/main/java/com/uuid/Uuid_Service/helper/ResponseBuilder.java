package com.uuid.Uuid_Service.helper;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ResponseBuilder {
    public static <T>ResponseEntity<ResponseWrapper<T>> success(String message, T data, HttpStatus httpStatus){
        return ResponseEntity.status(httpStatus)
                .body(new ResponseWrapper<>("success",message,data));
    }
}
