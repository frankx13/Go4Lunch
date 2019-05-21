package com.lepanda.studioneopanda.go4lunch.events;

import com.lepanda.studioneopanda.go4lunch.models.Restaurant;

public class NavToDetailEvent {

    private Restaurant mRestaurant;

    public NavToDetailEvent(Restaurant mRestaurant) {
        this.mRestaurant = mRestaurant;
    }

    public Restaurant getmRestaurant() {
        return mRestaurant;
    }

    public void setmRestaurant(Restaurant mRestaurant) {
        this.mRestaurant = mRestaurant;
    }
}
