package com.lepanda.studioneopanda.go4lunch.events;

import com.google.android.libraries.places.api.model.Place;

public class SearchPlaceEvent {

    Place place;

    public SearchPlaceEvent(Place place) {
        this.place = place;
    }

    public Place getPlace() {
        return place;
    }

    public void setPlace(Place place) {
        this.place = place;
    }
}
