package com.example.demo.services;

import com.example.demo.models.Poll;
import com.example.demo.models.User;
import com.example.demo.models.Vote;
import com.example.demo.repositories.VoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VoteService {

    @Autowired
    private VoteRepository voteRepository;

    // Check if the user has already voted on a specific poll
    public boolean hasUserVoted(Long pollId, Long userId) {
        return voteRepository.existsByPollIdAndUserId(pollId, userId);
    }

    // Save a vote
    public Vote saveVote(User user, Poll poll, String optionCaption) {
        Vote vote = new Vote(user, poll, optionCaption);
        return voteRepository.save(vote);
    }
    public void deleteAllVotes() {
        voteRepository.deleteAll();
    }
}
