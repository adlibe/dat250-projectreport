package com.analyticscomponent.analyticscomponent;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.data.mongodb.core.MongoTemplate;

/*
 * This class listens for messages from RabbitMQ on the pollQueue
 * It processes incoming messages and stores them in MongoDB
 */
@Component
public class RabbitMQListener {

    private final MongoTemplate mongoTemplate;

    @Autowired
    public RabbitMQListener(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @RabbitListener(queues = "pollQueue")
    public void receiveMessage(PollData pollData) {
        System.out.println("Data fra RMQ: " + pollData);
        mongoTemplate.save(pollData);
    }
}
