package com.Api.Helpers;


import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ResponseBuilder {

    public static <T> ResponseEntity<ResponceApi<T>> sucess(String message, T data){

        return ResponseEntity.ok(new ResponceApi<>("sucess",message,data));
    }

    public  static <T> ResponseEntity<ResponceApi<T>> error(String message, HttpStatus status){
        return ResponseEntity.status(status).body(new ResponceApi<>("error",message,null));
    }

    public  static  ResponseEntity<ApiLoginResponse> loginSucess(String message,String token){
        return ResponseEntity.ok(new ApiLoginResponse("success",message,token));
    }

    public static ResponseEntity<ApiLoginResponse> loginError(String message,HttpStatus httpStatus){
        return ResponseEntity.status(httpStatus).body( new ApiLoginResponse("error",message,null));
    }
}
