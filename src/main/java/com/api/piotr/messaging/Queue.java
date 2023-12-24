package com.api.piotr.messaging;

import static com.api.piotr.messaging.Topic.EVENT_TOPIC;

public interface Queue {
    public static final String GENERAL_QUEUE = "general";
    public static final String PRODUCT_QUEUE = "product";
    public static final String IMAGE_QUEUE = "image";
    public static final String PIOTR_AMQ_QUEUE = "piotr-amq";
    public static final String PIOTR_AMQ_QUEUE_COMPLET = EVENT_TOPIC + "::" + PIOTR_AMQ_QUEUE;
}
