package com.analyticscomponent.analyticscomponent;

import java.io.Serializable;

/*
 * This class represents a poll option in a poll.
 */
public class PollOptionData implements Serializable {
    private String caption;
    private int voteCount;

    public PollOptionData(String caption, int voteCount) {
        this.caption = caption;
        this.voteCount = voteCount;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public int getVoteCount() {
        return voteCount;
    }

    public void setVoteCount(int voteCount) {
        this.voteCount = voteCount;
    }

    @Override
    public String toString() {
        return "PollOptionData{" +
                "caption='" + caption + '\'' +
                ", voteCount=" + voteCount +
                '}';
    }
}
