package com.example.trip.models;

import java.io.Serializable;

/**
 * Created by Toka on 2019-02-17.
 */
public class TripLocation implements Serializable {
    private double lat;
    private double lng;
    private String address;

    public TripLocation() {
    }

    public TripLocation(double lat, double lng, String address) {
        this.lat = lat;
        this.lng = lng;
        this.address = address;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
