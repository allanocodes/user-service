package com.email_service.consumer;


import com.email_service.Dao.ProfileRepo;
import com.email_service.Dao.UserManagementRepo;
import com.email_service.dto.MessageUser;
import com.email_service.dto.User;
import com.email_service.dto.UserProfile;
import com.email_service.service.EmailService;
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
    UserManagementRepo userManagementRepo;


    @Autowired
    ProfileRepo profileRepo;



    private static final Logger LOGGER=  LoggerFactory.getLogger(RabbitMqConsumer.class);

    @RabbitListener(queues = {"${rabbitmq.json-queue.name}"})
    public void jsonConsumer(MessageUser messageUser){
        LOGGER.info(String.format("Json payload received :%s",messageUser.toString()));

        if(!messageUser.getEmail().equals("null")){
            emailService.sendSimpleMail(messageUser.getEmail(), messageUser.getSubject(), messageUser.getText());
        }

        else {

            Optional<User> userOptional = userManagementRepo.getUserByUsername(messageUser.getUsername());
            User user = userOptional.get();
            UserProfile profile= user.getProfile();
            profile.setEmail_sent(true);
            profileRepo.save(profile);


            emailService.sendSimpleMail(user.getProfile().getEmail(),messageUser.getSubject(),messageUser.getText());

        }


    }



}
