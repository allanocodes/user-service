package com.sms_service.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitmqConfig {

    @Value("${rabbitmq.queue.name}")
    private String queueName;

    @Value("${rabbitmq.queue.exchange}")
    private String exchange;

    @Value("${rabbitmq.queue.routing-key}")
    private String routingKey;


    @Bean
   public Queue queue(){
       return new Queue(queueName);
   }

   @Bean
   public TopicExchange exchange(){
   return new TopicExchange(exchange);
   }

   @Bean
   public Binding binding(){
       return BindingBuilder.bind(queue())
               .to(exchange())
               .with(routingKey);
   }

   @Bean
   AmqpTemplate amqpTemplate(ConnectionFactory connectionFactory){
       RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
       rabbitTemplate.setMessageConverter(converter());
       return rabbitTemplate;
   }


   @Bean
   public MessageConverter converter(){
        return new Jackson2JsonMessageConverter();
   }

}
