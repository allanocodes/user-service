package com.Api.controller;

import com.Api.Exceptions.*;
import com.Api.Helpers.ResponceApi;
import com.Api.Helpers.ResponseErrorApi;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalControllerHandler {

    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<ResponceApi<String>> handleDatabaseError(DataAccessException ex){
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ResponceApi<>("error",ex.getMessage(),null));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponceApi<String>> handleGeneric(){
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ResponceApi<>("error","Unexpected error Ocurred",null));
    }

    @ExceptionHandler(InvalidTokenException.class)
    public ResponseEntity<ResponseErrorApi<String>>  handleSignatureError(InvalidTokenException ex){

        Throwable cause = ex.getCause();
         String message;
        if (cause instanceof ExpiredJwtException) {
            message = "Token has expired";
        } else if (cause instanceof SignatureException) {
            message = "Token signature is invalid";
        } else if (cause instanceof MalformedJwtException) {
            message = "Token is malformed";
        } else if (cause instanceof UnsupportedJwtException) {
            message = "Token type is unsupported";
        } else if (cause instanceof IllegalArgumentException) {
            message = "Token is null or empty";
        } else {  message = "Invalid token";
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ResponseErrorApi<>("error",message,null));
    }


    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ResponceApi<String>>  handleResourceDoesNotExist(){
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ResponceApi<>("error","Record Does Not Exist",null));
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
   public ResponseEntity<ResponseErrorApi<Map<String,String>>>  handleValidation(MethodArgumentNotValidException ex){
      Map<String,String>  errors = new HashMap<>();
      ex.getBindingResult().getFieldErrors().forEach( e->{
          errors.put(e.getField(),e.getDefaultMessage());
      });
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body( new ResponseErrorApi<>("error","Invalid input",errors));
   }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ResponseErrorApi<String>> handleBadRequest(HttpMessageNotReadableException ex) {
        Throwable rootCause = ex.getMostSpecificCause();

        if (rootCause instanceof WrongPhoneFormat) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseErrorApi<>(
                            "error",
                            rootCause.getMessage(),
                            "phone format should be like +254-123456789"));
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ResponseErrorApi<>(
                        "error",
                        "Malformed JSON request",
                        null));
    }




}
