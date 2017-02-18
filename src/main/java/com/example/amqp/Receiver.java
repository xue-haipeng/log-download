package com.example.amqp;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * Created by Xue on 02/18/17.
 */
@Component
public class Receiver {

    @RabbitListener(queues = "hello")
    public void receive( String message) {
        System.out.println("********** Received Message: " + message);
    }
}
