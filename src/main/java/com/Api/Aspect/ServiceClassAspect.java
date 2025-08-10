package com.Api.Aspect;

import com.Api.Entity.User;
import com.Api.Exceptions.ServiceException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
@Aspect
public class ServiceClassAspect {

   @Around("execution(* com.Api.service.UserService.*(..))")
    public Object wrapServiceException(ProceedingJoinPoint joinPoint) throws Throwable {
        try{
            System.out.println("Method: " + joinPoint.getSignature());
         return  joinPoint.proceed();

        }catch (DataAccessException e){
            throw new ServiceException("Database Errors Occured",e);
        }



    }
}
