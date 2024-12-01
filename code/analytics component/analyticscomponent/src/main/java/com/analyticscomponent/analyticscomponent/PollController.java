package com.analyticscomponent.analyticscomponent;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/*
 * This is the REST controller for managing poll-related endpoints
 * It exposes endpoints to fetch polls and create new ones
 */
@RestController
@RequestMapping("/polls")
public class PollController {

    private final RabbitTemplate rabbitTemplate;

    @Autowired
    public PollController(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @PostMapping
    public String createPoll(@RequestBody PollData pollData) {
        rabbitTemplate.convertAndSend("pollQueue", pollData);
        return "Poll sendt!";
    }
}
