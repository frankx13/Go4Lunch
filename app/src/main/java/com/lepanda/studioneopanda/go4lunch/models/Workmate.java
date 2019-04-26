package com.lepanda.studioneopanda.go4lunch.models;

public class Workmate {

    private String uid;
    private String username;
    private String urlPicture;

    public Workmate(){
        //empty constructor needed for Firebase
    }

    public Workmate(String username, String urlPicture, String uid) {
        this.username = username;
        this.urlPicture = urlPicture;
        this.uid = uid;
    }

    public String getUsername() {
        return username;
    }

    public String getUid() {
        return uid;
    }

    public String getUrlPicture() {
        return urlPicture;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setUrlPicture(String urlPicture) {
        this.urlPicture = urlPicture;
    }
}
