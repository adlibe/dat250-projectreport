package com.example.demo;

import com.example.demo.models.Poll;
import com.example.demo.models.User;
import com.example.demo.models.Vote;
import com.example.demo.models.VoteOption;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class PollManager {

    private Map<String, Poll> polls;
    private Map<String, User> users;

    public PollManager() {
        this.polls = new HashMap<>();
        this.users = new HashMap<>();
    }

    // Poll Management Methods

    public Poll createPoll(Poll poll) {
        String pollId = UUID.randomUUID().toString();
        poll.setId(pollId);
        polls.put(pollId, poll);
        return poll;
    }

    public Poll getPollById(String id) {
        return polls.get(id);
    }

    public boolean deletePoll(String id) {
        if (polls.containsKey(id)) {
            polls.remove(id);
            return true;
        }
        return false;
    }

    public void addVote(String pollId, Vote vote) {
        Poll poll = polls.get(pollId);
        if (poll != null) {
            poll.getVotes().add(vote);
        }
    }

    public List<Poll> getAllPolls() {
        return new ArrayList<>(polls.values());
    }

    public User createUser(User user) {
        user.setId(UUID.randomUUID().toString());  // Generate a unique ID for the user
        users.put(user.getId(), user);  // Save the user in the map
        return user;
    }


    public User getUserById(String id) {
        return users.get(id);
    }

    public void addPoll(Poll poll) {
        polls.put(poll.getId(), poll);
    }

    public List<User> getAllUsers() {
        return new ArrayList<>(users.values());  // Convert Map<String, User> to List<User>
    }

    public void clearAll() {
        polls.clear();
        users.clear();
    }
}
