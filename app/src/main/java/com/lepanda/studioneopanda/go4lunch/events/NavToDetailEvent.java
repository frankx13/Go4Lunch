package com.lepanda.studioneopanda.go4lunch.events;

public class NavToDetailEvent {

    String mRestName;
    Boolean isReceivedFromList;

    public NavToDetailEvent(String mRestName, Boolean isReceivedFromList) {
        this.mRestName = mRestName;
        this.isReceivedFromList = isReceivedFromList;
    }

    public Boolean getReceivedFromList() {
        return isReceivedFromList;
    }

    public void setReceivedFromList(Boolean receivedFromList) {
        isReceivedFromList = receivedFromList;
    }

    public String getmRestName() {
        return mRestName;
    }

    public void setmRestName(String mRestName) {
        this.mRestName = mRestName;
    }
}
