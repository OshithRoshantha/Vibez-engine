package com.vibez.engine.RabbitMQ.Consumer;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vibez.engine.Model.RabbitMQ;

@Service
public class RabbitMQConsumer {

 
    @RabbitListener(queues = "${rabbitmq.queue.name}")
    public void consumeMessage(String message) {
        RabbitMQ messageObj = null;
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            messageObj = objectMapper.readValue(message, RabbitMQ.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
