package com.Api.service;


import jakarta.mail.AuthenticationFailedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;


     public void sendSimpleMail(String email,String subject,String text){
         SimpleMailMessage message = new SimpleMailMessage();
         message.setFrom("allanwrites4u@gmail.com");
         message.setTo(email);
         message.setSubject(subject);
         message.setText(text);
         try {
             mailSender.send(message);
         } catch (MailException e) {
             System.out.println("Mail sending error: " + e.getMessage());
         }
     }
}
