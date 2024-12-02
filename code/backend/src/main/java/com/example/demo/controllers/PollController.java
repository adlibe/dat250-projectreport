package com.example.demo.controllers;

import com.example.demo.models.Poll;
import com.example.demo.models.User;
import com.example.demo.services.PollService;
import com.example.demo.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;

@RestController
@RequestMapping("/api/polls")
@CrossOrigin(origins = "http://localhost:5173")
public class PollController {

    @Autowired
    private PollService pollService;

    @Autowired
    private UserService userService; // For token validation and user retrieval

    // Get all polls (Public access)
    @GetMapping(produces = "application/json")
    public ResponseEntity<Iterable<Poll>> getPolls() {
        return ResponseEntity.ok(pollService.getAllPolls());
    }

    // Create a new poll (Authenticated users only)
    @PostMapping(consumes = "application/json", produces = "application/json")
    public ResponseEntity<?> createPoll(
            @RequestBody Poll poll,
            @RequestHeader("Authorization") String token) {
        User user = userService.validateTokenAndGetUser(token); // Validate token and get user
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("You must be logged in to create a poll.");
        }

        poll.setCreator(user); // Assign the authenticated user as the poll creator
        poll.setPublishedAt(Instant.now());
        Poll createdPoll = pollService.savePoll(poll);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdPoll);
    }

    // Delete a poll by ID (Only the creator can delete)
    @DeleteMapping("/{pollId}")
    public ResponseEntity<?> deletePoll(
            @PathVariable Long pollId,
            @RequestHeader("Authorization") String token) {
        User user = userService.validateTokenAndGetUser(token);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("You must be logged in to delete a poll.");
        }

        Poll poll = pollService.getPollById(pollId);
        if (poll != null && poll.getCreator().getId().equals(user.getId())) {
            pollService.deletePoll(pollId);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You can only delete your own polls.");
    }

    // Vote on a poll (Authenticated users only)
    @PostMapping("/{pollId}/vote")
    public ResponseEntity<?> voteOnPoll(
            @PathVariable Long pollId,
            @RequestParam("option") String option,
            @RequestHeader("Authorization") String token) {
        User user = userService.validateTokenAndGetUser(token);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("You must be logged in to vote.");
        }

        boolean voteSuccess = pollService.voteOnPoll(pollId, option, user);
        if (voteSuccess) {
            return ResponseEntity.ok("Vote registered successfully.");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to register vote. Ensure the poll and option are valid.");
        }
    }
}
