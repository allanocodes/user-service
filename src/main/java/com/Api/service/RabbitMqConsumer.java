package com.Api.service;

import com.Api.Dao.UserRepositories;
import com.Api.Dto.MessageUser;
import com.Api.Dto.UserDto;
import com.Api.Entity.User;
import org.aspectj.bridge.Message;
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
    UserRepositories userRepositories;



    private static final Logger LOGGER=  LoggerFactory.getLogger(RabbitMqConsumer.class);

    @RabbitListener(queues = {"${rabbitmq.queue.name}"})
    public void consume(String message){
      LOGGER.info(String.format("Message received: %s",message));
    }

    @RabbitListener(queues = {"${rabbitmq.json-queue.name}"})
    public void jsonConsumer(MessageUser messageUser){
        LOGGER.info(String.format("Json payload received :%s",messageUser.toString()));

        if(messageUser.getEmail() != null){
            emailService.sendSimpleMail(messageUser.getEmail(), messageUser.getSubject(), messageUser.getText());
        }

        else {
            Optional<User> user = userRepositories.getUserByUsername(messageUser.getUsername());

            User user1 = user.orElseGet(() -> {
                return null;
            });

            String email = user1.getProfile().getEmail();

            emailService.sendSimpleMail(email, messageUser.getSubject(), messageUser.getText());

        }


    }



}
