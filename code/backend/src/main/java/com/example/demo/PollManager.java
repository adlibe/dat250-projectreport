package com.example.demo;

import com.example.demo.models.Poll;
import com.example.demo.models.User;
import com.example.demo.models.Vote;
import com.example.demo.services.PollService;
import com.example.demo.services.UserService;
import com.example.demo.services.VoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PollManager {

    @Autowired
    private PollService pollService;

    @Autowired
    private UserService userService;

    @Autowired
    private VoteService voteService;

    // Delegate poll creation to PollService
    public Poll createPoll(Poll poll) {
        return pollService.savePoll(poll);
    }

    // Delegate fetching all polls to PollService
    public List<Poll> getAllPolls() {
        return (List<Poll>) pollService.getAllPolls();
    }

    // Delegate poll deletion to PollService
    public boolean deletePoll(Long pollId) {
        Poll poll = pollService.getPollById(pollId);
        if (poll != null) {
            pollService.deletePoll(pollId);
            return true;
        }
        return false;
    }

    // Add a vote to a poll
    public void addVote(Long pollId, Vote vote) {
        Poll poll = pollService.getPollById(pollId);
        if (poll != null) {
            voteService.saveVote(vote.getUser(), poll, vote.getOptionCaption());
        }
    }

    // Delegate user creation to UserService
    public User createUser(User user) {
        return userService.registerUser(user);
    }

    // Delegate fetching all users to UserService
    public List<User> getAllUsers() {
        return (List<User>) userService.getAllUsers();
    }

    // Clear all polls, users, and votes
    public void clearAll() {
        voteService.deleteAllVotes();  // Ensure VoteService has a method to delete all votes
        pollService.deleteAllPolls(); // Ensure PollService has a method to delete all polls
        userService.deleteAllUsers(); // Ensure UserService has a method to delete all users
    }
}
