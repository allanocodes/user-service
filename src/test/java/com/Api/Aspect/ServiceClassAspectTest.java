package com.Api.Aspect;

import com.Api.Exceptions.ServiceException;
import com.Api.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ServiceClassAspectTest {
    @Autowired
    UserService service;

    @Test
   void nullPointerExceptionAspectTest(){
        Exception exception = assertThrows(ServiceException.class,()->{
            service.throwError();
        });

        assertTrue(exception.getMessage().contains("Unexpected error ocurred"));

   }

}