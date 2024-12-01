package com.example.demo.controllers;

import com.example.demo.PollManager;
import com.example.demo.models.Poll;
import com.example.demo.models.Vote;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.Map;

@RestController
@RequestMapping("/polls")
@CrossOrigin(origins = "http://localhost:5173")
public class VoteController {

    @Autowired
    private PollManager pollManager;

    @PostMapping("/{pollId}/vote")
    public ResponseEntity<Void> voteOnPoll(
            @PathVariable String pollId,
            @RequestBody Map<String, String> votePayload) {

        String userId = votePayload.get("userId");
        String optionCaption = votePayload.get("option");
        String voteType = votePayload.get("voteType");  // "upvote" or "downvote"

        Poll poll = pollManager.getPollById(pollId);
        if (poll == null) {
            return ResponseEntity.notFound().build();
        }

        // Handling upvotes
        if (voteType.equals("upvote")) {
            Vote existingVote = poll.getVotes().stream()
                    .filter(v -> v.getUserId().equals(userId) && v.getOptionCaption().equals(optionCaption))
                    .findFirst()
                    .orElse(null);

            if (existingVote != null) {
                // Update the existing vote to an upvote if the user already voted
                existingVote.setVoteType("upvote");
                existingVote.setPublishedAt(Instant.now());
            } else {
                // Create a new vote if no vote exists from the user
                Vote newVote = new Vote(userId, optionCaption, "upvote");
                poll.getVotes().add(newVote);
            }
        }

        // Handling downvotes (any user can reduce the count of an option)
        if (voteType.equals("downvote")) {
            // Get all votes for the option, regardless of user
            Vote existingVote = poll.getVotes().stream()
                    .filter(v -> v.getOptionCaption().equals(optionCaption))
                    .findFirst()
                    .orElse(null);

            if (existingVote != null) {
                // Remove one vote from the option
                poll.getVotes().remove(existingVote);  // Remove the vote from the total count of that option
            }
        }

        pollManager.addPoll(poll);  // Save the poll after updating the votes
        return ResponseEntity.ok().build();
    }

}
