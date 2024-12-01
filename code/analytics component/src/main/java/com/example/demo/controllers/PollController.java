package com.example.demo.controllers;

import com.example.demo.models.PollData;
import com.example.demo.models.PollOptionData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.demo.PollManager;
import com.example.demo.models.Poll;
import com.example.demo.models.Vote;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/polls")
@CrossOrigin
public class PollController {

    @Autowired
    private PollManager pollManager;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    // Get all polls
    @GetMapping(produces = "application/json")
    public ResponseEntity<List<Poll>> getPolls() {
        List<Poll> polls = pollManager.getAllPolls();
        return ResponseEntity.ok(polls);
    }

    // Create a new poll
    @PostMapping(consumes = "application/json", produces = "application/json")
    public ResponseEntity<Poll> createPoll(@RequestBody Poll poll) {
        poll.setPublishedAt(Instant.now());
        Poll createdPoll = pollManager.createPoll(poll);

        List<PollOptionData> optionDataList = createdPoll.getOptions().stream()
                .map(option -> new PollOptionData(option.getCaption()))
                .collect(Collectors.toList());

        PollData pollData = new PollData(createdPoll.getId(), createdPoll.getQuestion(), optionDataList, 0);
        rabbitTemplate.convertAndSend("pollQueue", pollData);


        return ResponseEntity.status(201).body(createdPoll);
    }

    // Delete a poll by ID
    @DeleteMapping("/{pollId}")
    public ResponseEntity<Void> deletePoll(@PathVariable String pollId) {
        if (pollManager.deletePoll(pollId)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
