package com.email_service.consumer;


import com.email_service.dto.DisplayDto;
import com.email_service.dto.MessageUser;
import com.email_service.dto.User;
import com.email_service.service.EmailService;
import com.email_service.service.SecurityInterface;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
@Profile("!test")
public class RabbitMqConsumer {

    @Autowired
    EmailService emailService;

    @Autowired
    SecurityInterface securityInterface;
    

    private static final Logger LOGGER=  LoggerFactory.getLogger(RabbitMqConsumer.class);

    @RabbitListener(queues = {"${rabbitmq.json-queue.name}"})
    public void jsonConsumer(MessageUser messageUser){
        LOGGER.info(String.format("Json payload received :%s",messageUser.toString()));

        if(!messageUser.getEmail().equals("null")){
            emailService.sendSimpleMail(messageUser.getEmail(), messageUser.getSubject(), messageUser.getText());
        }

        else {

            DisplayDto displayDto = securityInterface.findByUsername(messageUser.getUsername()).getBody().getData();
            emailService.sendSimpleMail(displayDto.getEmail(),messageUser.getSubject(),messageUser.getText());
        }


    }



}
