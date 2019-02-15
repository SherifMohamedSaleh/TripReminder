package com.example.trip.models;

import android.location.Location;

import java.time.LocalDate;
import java.util.Date;
import java.util.HashMap;

public class TripData {
    int tripType;
    HashMap<String, Boolean> notesHmap = new HashMap<String, Boolean>();
    private String tripName;
    private String startPoint;
    private String endPoint;
    private LocalDate date;

    public TripData(String tripName, String startPoint, String endPoint, int tripType ) {
        this.tripName = tripName;
        this.startPoint = startPoint;
        this.endPoint = endPoint;
        this.date = date;
        this.tripType = tripType;
    }

    public String getTripName() {
        return tripName;
    }

    public void setTripName(String tripName) {
        this.tripName = tripName;
    }

    public String getStartPoint() {
        return startPoint;
    }

    public void setStartPoint(String startPoint) {
        this.startPoint = startPoint;
    }

    public String getEndPoint() {
        return endPoint;
    }

    public void setEndPoint(String endPoint) {
        this.endPoint = endPoint;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public int getTripType() {
        return tripType;
    }

    public void setTripType(int tripType) {
        this.tripType = tripType;
    }

    public HashMap<String, Boolean> getNotesHmap() {
        return notesHmap;
    }

    public void setNotesHmap(HashMap<String, Boolean> notesHmap) {
        this.notesHmap = notesHmap;
    }
}
