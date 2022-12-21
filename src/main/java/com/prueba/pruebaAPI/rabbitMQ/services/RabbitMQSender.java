package com.prueba.pruebaAPI.rabbitMQ.services;

import com.prueba.pruebaAPI.dominio.Comprobante;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class RabbitMQSender {

    @Autowired
    private AmqpTemplate rabbitTemplate;
    
    @Value("${spring.rabbitmq.exchange}")
    private String exchange;
    
    @Value("${spring.rabbitmq.routingkey}")
    private String routingkey;
    
    public Comprobante send(Comprobante comprobante){
        return (Comprobante) rabbitTemplate.convertSendAndReceive(exchange, routingkey, comprobante);
    }
}
