package com.prueba.pruebaAPI.rabbitMQ.services;

import com.prueba.pruebaAPI.dominio.Comprobante;
import com.prueba.pruebaAPI.services.ComprobanteService;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Service
public class RabbitMQReciever {
    
    @Autowired
    private AmqpTemplate rabbitTemplate;
    
    @Value("${spring.rabbitmq.exchange}")
    private String exchange;
    
    @Value("${spring.rabbitmq.routingkey}")
    private String routingkey;
    
    @Autowired
    private ComprobanteService comprobanteService;
    
    @RabbitListener(queues = "${spring.rabbitmq.queue}")
    public Comprobante recieve(@Payload Comprobante comprobante){
        return comprobanteService.obtenerJson(comprobante);
    }
}