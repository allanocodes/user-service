package com.email_service.exception;



public class WrongPhoneFormat extends RuntimeException{
  public WrongPhoneFormat(String message){
      super(message);
  }
  public WrongPhoneFormat(String message, Throwable cause){
      super(message,cause);
  }
}
