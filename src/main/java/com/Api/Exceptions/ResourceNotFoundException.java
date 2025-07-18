package com.Api.Exceptions;

public class ResourceNotFoundException extends RuntimeException{
    public ResourceNotFoundException(String message){
        super(message);
    }
    public ResourceNotFoundException(String msg,Throwable cause){
        super(msg,cause);
    }
}
