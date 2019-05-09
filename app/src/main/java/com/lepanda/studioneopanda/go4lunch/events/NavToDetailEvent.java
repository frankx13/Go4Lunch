package com.lepanda.studioneopanda.go4lunch.events;

import com.lepanda.studioneopanda.go4lunch.models.Restaurant;

import java.util.List;

public class NavToDetailEvent {

    private String mRestName;
    private Boolean isReceivedFromList;
    private List<Restaurant> mDataRestaurants;

    public NavToDetailEvent(String mRestName, Boolean isReceivedFromList, List<Restaurant> mDataRestaurants) {
        this.mRestName = mRestName;
        this.isReceivedFromList = isReceivedFromList;
        this.mDataRestaurants = mDataRestaurants;
    }

    public List<Restaurant> getmDataRestaurants() {
        return mDataRestaurants;
    }

    public void setmDataRestaurants(List<Restaurant> mDataRestaurants) {
        this.mDataRestaurants = mDataRestaurants;
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

    public void setmRestName(String restName) {
        mRestName = restName;
    }
}
