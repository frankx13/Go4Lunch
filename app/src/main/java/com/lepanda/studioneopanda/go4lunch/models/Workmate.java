package com.lepanda.studioneopanda.go4lunch.models;

public class Workmate {

    private String uid;
    private String username;
    private String urlPicture;
    private String restSelection;

    public Workmate() {
        //empty constructor needed for Firebase
    }

    public Workmate(String uid, String username, String urlPicture, String restSelection) {
        this.uid = uid;
        this.username = username;
        this.urlPicture = urlPicture;
        this.restSelection = restSelection;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUrlPicture() {
        return urlPicture;
    }

    public void setUrlPicture(String urlPicture) {
        this.urlPicture = urlPicture;
    }

    public String getRestSelection() {
        return restSelection;
    }

    public void setRestSelection(String restSelection) {
        this.restSelection = restSelection;
    }
}
