package com.api.piotr.messaging;

import static com.api.piotr.constant.Profile.AMQ;
import static com.api.piotr.messaging.Queue.GENERAL_QUEUE;
import static com.api.piotr.messaging.Queue.IMAGE_QUEUE;
import static com.api.piotr.messaging.Queue.PRODUCT_QUEUE;

import org.springframework.context.annotation.Profile;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Profile(AMQ)
@Component
public class JmsMessageConsumer {

    @JmsListener(destination = GENERAL_QUEUE)
    public void receiveGeneral(String message) {
        System.out.println("Received general message: " + message);
    }

    @JmsListener(destination = PRODUCT_QUEUE)
    public void receiveProduct(String message) {
        System.out.println("Received product message: " + message);
    }

    @JmsListener(destination = IMAGE_QUEUE)
    public void receiveImage(String message) {
        System.out.println("Received image message: " + message);
    }
}
