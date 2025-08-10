package com.sms_service.controller;

import com.sms_service.Dto.SmsRequest;
import com.sms_service.service.SmsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("sms")
public class SmsController {

    @Autowired
    SmsService service;

    @PostMapping("/send")
    public void sendMessage(@RequestBody SmsRequest smsRequest){
      service.sendSms(smsRequest);
    }
}
