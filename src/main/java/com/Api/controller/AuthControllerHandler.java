package com.Api.controller;

import com.Api.Helpers.ApiLoginResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class AuthControllerHandler {

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ApiLoginResponse>  handleWrongPassword(){
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).
                body(new ApiLoginResponse("error","Wrong Password",null));
    }
    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ApiLoginResponse>  handleWrongUsername(){

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new ApiLoginResponse("error","Username Not Found",null));
    }
}
