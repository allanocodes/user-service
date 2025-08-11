package com.Api.Exceptions;



public class WrongPhoneFormat extends RuntimeException{
  public  WrongPhoneFormat(String message){
      super(message);
  }
  public WrongPhoneFormat(String message,Throwable cause){
      super(message,cause);
  }
}
