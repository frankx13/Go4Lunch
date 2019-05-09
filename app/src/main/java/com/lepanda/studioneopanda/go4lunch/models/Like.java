package com.lepanda.studioneopanda.go4lunch.models;

public class Like {

    private String RId;
    private String userLiker;

    public Like(String RId, String userLiker) {
        this.RId = RId;
        this.userLiker = userLiker;
    }

    public String getUserLiker() {
        return userLiker;
    }

    public void setUserLiker(String userLiker) {
        this.userLiker = userLiker;
    }

    public String getRId() {
        return RId;
    }

    public void setRId(String RId) {
        this.RId = RId;
    }
}
