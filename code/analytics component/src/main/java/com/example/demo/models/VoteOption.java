package com.example.demo.models;

public class VoteOption {
    private String caption;  // Option text
    private int presentationOrder;  // Order of presentation in the UI

    public VoteOption() {}

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public int getPresentationOrder() {
        return presentationOrder;
    }

    public void setPresentationOrder(int presentationOrder) {
        this.presentationOrder = presentationOrder;
    }
}
