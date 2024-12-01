package com.example.demo.models;

import java.io.Serializable;
import java.util.List;

public class PollData implements Serializable {
    private String pollId;
    private String question;
    private List<PollOptionData> options;
    private int voteCount;

    public PollData(String pollId, String question, List<PollOptionData> options, int voteCount) {
        this.pollId = pollId;
        this.question = question;
        this.options = options;
        this.voteCount = voteCount;
    }

    public String getPollId() {
        return pollId;
    }

    public void setPollId(String pollId) {
        this.pollId = pollId;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public List<PollOptionData> getOptions() {
        return options;
    }

    public void setOptions(List<PollOptionData> options) {
        this.options = options;
    }

    public int getVoteCount() {
        return voteCount;
    }

    public void setVoteCount(int voteCount) {
        this.voteCount = voteCount;
    }

    @Override
    public String toString() {
        return "PollData{" +
                "pollId='" + pollId + '\'' +
                ", question='" + question + '\'' +
                ", options=" + options +
                ", voteCount=" + voteCount +
                '}';
    }
}

