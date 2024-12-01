package com.example.demo.models;

import java.io.Serializable;

public class PollOptionData implements Serializable {
    private String caption;

    public PollOptionData(String caption) {
        this.caption = caption;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    @Override
    public String toString() {
        return "PollOptionData{" +
                "caption='" + caption + '\'' +
                '}';
    }
}
