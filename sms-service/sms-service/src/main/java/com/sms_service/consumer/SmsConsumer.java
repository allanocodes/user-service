package com.sms_service.consumer;

import com.sms_service.Dto.SmsRequest;
import com.sms_service.service.SmsService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class SmsConsumer {

    @Value("${rabbitmq.queue.name}")
    private String queueName;

    @Autowired
    SmsService smsService;


    @RabbitListener(queues = "${rabbitmq.queue.name}")
    public void sendSms(SmsRequest request){
     smsService.sendSms(request);
    }

}
