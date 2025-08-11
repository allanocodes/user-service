package com.Api.service;

import com.Api.Dto.MessageUser;
import com.Api.Dto.SmsRequest;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@Profile("!test")
public class RabbitMqProducer {
    @Value("${rabbitmq.binding-key.name}")
    String routingKey;
    @Value("${rabbitmq.exchange.name}")
    String exchangeName;
    @Autowired
    RabbitTemplate rabbitTemplate;


    @Value("${rabbitmq.queue.sms-name}")
    private String queueName;

    @Value("${rabbitmq.queue.exchange}")
    private String exchange;

    @Value("${rabbitmq.queue.routing-key}")
    private String routingKey2;


    @Value("${rabbitmq.json-queue.name}")
    String jsonQueue;

    @Value("${rabbitmq.json-exchange.name}")
    String jsonExchange;

    @Value("${rabbitmq.json-binding-key}")
    String jsonBindingKey;


    public void sendMessage(String message){
        rabbitTemplate.convertAndSend(exchangeName,routingKey,message);
    }

    public void sendJsonMessage(MessageUser messageUser){
        rabbitTemplate.convertAndSend(jsonExchange,jsonBindingKey,messageUser);
    }

    public void sendSms(SmsRequest smsRequest){
     rabbitTemplate.convertAndSend(exchange,routingKey2,smsRequest);
    }



}
