package com.example.demo.models;

import java.time.Instant;

public class Vote {

    private String userId;
    private String optionCaption;
    private String voteType;  // upvote or downvote
    private Instant publishedAt;

    // Default constructor
    public Vote() {
        this.publishedAt = Instant.now();  // Automatically set the published date
    }

    // Constructor with parameters
    public Vote(String userId, String optionCaption, String voteType) {
        this.userId = userId;
        this.optionCaption = optionCaption;
        this.voteType = voteType;
        this.publishedAt = Instant.now();  // Automatically set the published date
    }

    // Getter and setter for userId
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    // Getter and setter for optionCaption
    public String getOptionCaption() {
        return optionCaption;
    }

    public void setOptionCaption(String optionCaption) {
        this.optionCaption = optionCaption;
    }

    // Getter and setter for voteType
    public String getVoteType() {
        return voteType;
    }

    public void setVoteType(String voteType) {
        this.voteType = voteType;
    }

    // Getter and setter for publishedAt
    public Instant getPublishedAt() {
        return publishedAt;
    }

    public void setPublishedAt(Instant publishedAt) {
        this.publishedAt = publishedAt;
    }

    // New method for setting the option directly
    public void setOption(String option) {
        this.optionCaption = option;
    }
}
