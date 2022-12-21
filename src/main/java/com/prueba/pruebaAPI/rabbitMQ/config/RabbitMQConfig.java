package com.prueba.pruebaAPI.rabbitMQ.config;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.*;
import org.springframework.context.annotation.*;

@Configuration
public class RabbitMQConfig {

    @Value("${spring.rabbitmq.queue}")
    String queue;
    
    @Value("${spring.rabbitmq.exchange}")
    String exchange;
    
    @Value("${spring.rabbitmq.routingkey}")
    private String routingkey;
    
    @Bean
    Queue queue(){
        return new Queue(queue, true, false, false);
    }
    
    @Bean
    DirectExchange exchange(){
        return new DirectExchange(exchange, true, false);
    }
    
    @Bean
    Binding binding(Queue queue, DirectExchange exchange){
        return BindingBuilder.bind(queue).to(exchange).with(routingkey);
    }
    
    @Bean
    public MessageConverter jsonMessageConverter(){
        return new Jackson2JsonMessageConverter();
    }
    
    @Bean
    public AmqpTemplate rabbitTemplateConverter(ConnectionFactory connectionFactory) {
        final RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(jsonMessageConverter());
        rabbitTemplate.setChannelTransacted(true);
        rabbitTemplate.setUserCorrelationId(true);
        rabbitTemplate.setReplyTimeout(300000);
        rabbitTemplate.setReceiveTimeout(200000);
        return rabbitTemplate;
    }
    
    @Bean
    public SimpleRabbitListenerContainerFactory customRabbitListenerContainterFactory(ConnectionFactory connectionFactory) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        try {
            factory.setConnectionFactory(connectionFactory);
            factory.setMessageConverter(jsonMessageConverter());
        } catch (Exception ex) {
            Logger.getLogger(RabbitMQConfig.class.getName()).log(Level.SEVERE, null, ex);
        }
        return factory;
    }
}
