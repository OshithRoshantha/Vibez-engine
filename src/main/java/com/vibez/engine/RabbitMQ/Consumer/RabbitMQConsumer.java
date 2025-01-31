package com.vibez.engine.RabbitMQ.Consumer;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class RabbitMQConsumer {
 
    @RabbitListener(queues = "${rabbitmq.queue.name}")
    public void consumeMessage(String message) {
        System.out.println("Message received from queue: " + message);
    }

}
