package com.lepanda.studioneopanda.go4lunch.models;

import com.firebase.ui.auth.data.model.User;
import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

public class Selection {

    private Boolean selection;
    private Date dateCreated;
    private User userSender;
    private String urlImage;

    public Selection() { }


    public Selection(Boolean selection, Date dateCreated, User userSender, String urlImage) {
        this.selection = selection;
        this.dateCreated = dateCreated;
        this.userSender = userSender;
        this.urlImage = urlImage;
    }

    // --- GETTERS ---

    public Boolean getSelection() { return selection; }
    @ServerTimestamp public Date getDateCreated() { return dateCreated; }
    public User getUserSender() { return userSender; }
    public String getUrlImage() { return urlImage; }

    // --- SETTERS ---

    public void setSelection(Boolean selection) { this.selection = selection; }

    public void setDateCreated(Date dateCreated) { this.dateCreated = dateCreated; }
    public void setUserSender(User userSender) { this.userSender = userSender; }
    public void setUrlImage(String urlImage) { this.urlImage = urlImage; }
}