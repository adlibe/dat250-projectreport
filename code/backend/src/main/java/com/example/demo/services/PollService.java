package com.example.demo.services;

import com.example.demo.models.Poll;
import com.example.demo.models.User;
import com.example.demo.models.Vote;
import com.example.demo.repositories.PollRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.demo.repositories.VoteRepository;

import java.util.List;

@Service
public class PollService {

    @Autowired
    private PollRepository pollRepository;

    @Autowired
    private VoteRepository voteRepository;

    // Save a new poll
    public Poll savePoll(Poll poll) {
        return pollRepository.save(poll);
    }

    // Get a poll by ID
    public Poll getPollById(Long pollId) {
        return pollRepository.findById(pollId).orElse(null);
    }

    // Get all polls
    public List<Poll> getAllPolls() {
        return pollRepository.findAll();
    }

    // Delete a poll by ID
    public void deletePoll(Long pollId) {
        pollRepository.deleteById(pollId);
    }
    public void deleteAllPolls() {
        pollRepository.deleteAll();
    }


    // Add a vote to a poll option
    public boolean voteOnPoll(Long pollId, String option, User user) {
        Poll poll = getPollById(pollId);
        if (poll == null) {
            return false; // Poll does not exist
        }

        // Ensure user has not already voted on this poll
        boolean alreadyVoted = voteRepository.existsByPollIdAndUserId(pollId, user.getId());
        if (alreadyVoted) {
            return false;
        }

        // Increment the vote count for the selected option
        poll.getOptions().forEach(opt -> {
            if (opt.getText().equalsIgnoreCase(option)) {
                opt.incrementVotes();
            }
        });

        // Save the vote
        Vote vote = new Vote(user, poll, option);
        voteRepository.save(vote);

        // Save the updated poll
        pollRepository.save(poll);
        return true;
    }
}