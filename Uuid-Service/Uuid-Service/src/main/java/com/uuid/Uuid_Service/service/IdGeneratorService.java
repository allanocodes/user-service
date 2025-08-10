package com.uuid.Uuid_Service.service;

import com.fasterxml.jackson.databind.DatabindException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;

@Service
public class IdGeneratorService {

    @Autowired
    StringRedisTemplate redisTemplate;

    public String generateId(String type){
        String dateString = LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE);

        String redisKey= type + ":" + dateString ;

        Long sequence = redisTemplate.opsForValue().increment(redisKey);

        String formattedString = String.format("%04d",sequence);

        return type + "-"  +  dateString + "-" + formattedString;

    }
}
