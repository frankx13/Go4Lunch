package com.lepanda.studioneopanda.go4lunch.events;

import com.lepanda.studioneopanda.go4lunch.models.Restaurant;

public class RefreshRVEvent {

    private Restaurant restaurant;

    public RefreshRVEvent(Restaurant restaurant) {
        this.restaurant = restaurant;
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }
}
