package com.example.demo.controllers;

import com.example.demo.models.Poll;
import com.example.demo.models.User;
import com.example.demo.services.PollService;
import com.example.demo.services.VoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;
import java.util.Map;

@RestController
@RequestMapping("/polls")
@CrossOrigin(origins = "http://localhost:5173")
public class VoteController {

    @Autowired
    private PollService pollService;

    @Autowired
    private VoteService voteService;

    // Submit a vote for a specific poll
    @PostMapping("/{pollId}/vote")
    public ResponseEntity<String> voteOnPoll(
            @PathVariable Long pollId,
            @RequestBody Map<String, String> votePayload,
            HttpSession session) {

        // Get the authenticated user from session
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return ResponseEntity.status(401).body("Unauthorized: Please log in to vote");
        }

        String optionCaption = votePayload.get("option");
        Poll poll = pollService.getPollById(pollId);

        if (poll == null) {
            return ResponseEntity.notFound().build();
        }

        // Check if the user has already voted
        boolean hasVoted = voteService.hasUserVoted(pollId, user.getId());
        if (hasVoted) {
            return ResponseEntity.badRequest().body("You have already voted on this poll");
        }

        // Save the vote
        voteService.saveVote(user, poll, optionCaption);
        return ResponseEntity.ok("Vote successfully recorded");
    }
}
