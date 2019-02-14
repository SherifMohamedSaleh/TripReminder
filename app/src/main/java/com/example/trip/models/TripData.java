package com.example.trip.models;

import java.util.Date;
import java.util.HashMap;

public class TripData {
    int tripType;
    HashMap<String, Boolean> notesHmap = new HashMap<String, Boolean>();
    private String tripName;
    private String startPoint;
    private String endPoint;
    private Date date;

    public TripData(String tripName, String startPoint, String endPoint, Date date, int tripType) {
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

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
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
