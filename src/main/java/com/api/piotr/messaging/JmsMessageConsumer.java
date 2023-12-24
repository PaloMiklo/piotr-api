package com.api.piotr.messaging;

import static com.api.piotr.constant.Profile.AMQ;
import static com.api.piotr.messaging.Queue.GENERAL_QUEUE;
import static com.api.piotr.messaging.Queue.IMAGE_QUEUE;
import static com.api.piotr.messaging.Queue.PIOTR_AMQ_QUEUE_COMPLET;
import static com.api.piotr.messaging.Queue.PRODUCT_QUEUE;

import org.springframework.context.annotation.Profile;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Profile(AMQ)
@Component
public class JmsMessageConsumer {

    @JmsListener(destination = PIOTR_AMQ_QUEUE_COMPLET)
    public void receiveEvent(String message) {
        log.debug("Received event: " + message);
    }

    @JmsListener(destination = GENERAL_QUEUE)
    public void receiveGeneral(String message) {
        log.debug("Received general message: " + message);
    }

    @JmsListener(destination = PRODUCT_QUEUE)
    public void receiveProduct(String message) {
        log.debug("Received product message: " + message);
    }

    @JmsListener(destination = IMAGE_QUEUE)
    public void receiveImage(String message) {
        log.debug("Received image message: " + message);
    }
}
