package com.Api.config;


import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.util.Collection;
import java.util.List;
import java.util.Map;

@Profile("!test")
@Configuration
public class RabbitmqConfig {
    @Value("${rabbitmq.queue.name}")
    String queue;

    @Value("${rabbitmq.exchange.name}")
    String exchange;

    @Value("${rabbitmq.binding-key.name}")
    String routingKey;

    @Value("${rabbitmq.json-queue.name}")
    String jsonQueue;

    @Value("${rabbitmq.json-exchange.name}")
    String jsonExchange;

    @Value("${rabbitmq.json-binding-key}")
    String jsonBindingKey;

    @Bean
    public Queue queue(){
        return new Queue(queue);
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
    public Queue jsonQueue(){
        return new Queue(jsonQueue);
    }
    @Bean
    public TopicExchange jsonExchange(){
        return new TopicExchange(jsonExchange);
    }
    @Bean
    public Binding jsonBinding(){
        return BindingBuilder.bind(jsonQueue())
                .to(jsonExchange())
                .with(jsonBindingKey);
    }

    @Bean
    public AmqpTemplate amqpTemplate(ConnectionFactory connectionFactory){
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
         rabbitTemplate.setMessageConverter(converter());
       return rabbitTemplate;

    }

    @Bean
    public MessageConverter converter(){
        return new Jackson2JsonMessageConverter();
    }
}
