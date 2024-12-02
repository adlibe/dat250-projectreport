package com.example.demo.models;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
public class Vote {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "poll_id", nullable = false)
    private Poll poll;

    private String optionCaption;
    private Instant createdAt;

    public Vote() {
        this.createdAt = Instant.now();
    }

    public Vote(User user, Poll poll, String optionCaption) {
        this.user = user;
        this.poll = poll;
        this.optionCaption = optionCaption;
        this.createdAt = Instant.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Poll getPoll() {
        return poll;
    }

    public void setPoll(Poll poll) {
        this.poll = poll;
    }

    public String getOptionCaption() {
        return optionCaption;
    }

    public void setOptionCaption(String optionCaption) {
        this.optionCaption = optionCaption;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }
}
