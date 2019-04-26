package com.lepanda.studioneopanda.go4lunch.models;

import android.graphics.Bitmap;

import com.google.android.gms.maps.model.LatLng;

import org.parceler.Parcel;

import java.util.List;

@Parcel
public class Restaurant {

    public String placeId;
    public String name;
    public String phoneNumber;
    public List<String> openingHours;
    public String websiteURI;
    public String types;
    public String address;
    public Double rating;
    public LatLng latlng;
    public Bitmap photos;
    public float distance;

    public float getDistance() {
        return distance;
    }

    public void setDistance(float distance) {
        this.distance = distance;
    }

    public Bitmap getPhotos() {
        return photos;
    }

    public void setPhotos(Bitmap photos) {
        this.photos = photos;
    }

    public List<String> getOpeningHours() {
        return openingHours;
    }

    public void setOpeningHours(List<String> openingHours) {
        this.openingHours = openingHours;
    }

    public String getWebsiteURI() {
        return websiteURI;
    }

    public void setWebsiteURI(String websiteURI) {
        this.websiteURI = websiteURI;
    }

    public LatLng getLatlng() {
        return latlng;
    }

    public void setLatlng(LatLng latlng) {
        this.latlng = latlng;
    }

    public String getTypes() {
        return types;
    }

    public void setTypes(String types) {
        this.types = types;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public String getPlaceId() {
        return placeId;
    }

    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
