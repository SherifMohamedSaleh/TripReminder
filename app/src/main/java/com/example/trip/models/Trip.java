package com.example.trip.models;

import java.io.Serializable;
import java.util.ArrayList;

public class Trip implements Serializable {
    private boolean isRoundedTrip;
    private ArrayList<Note> notes;
    private String tripName;
    private TripLocation endPoint;
    private TripLocation startPoint;
    private TripDate date;
    private TripTime time;
    private int tripRequestId;
    private String status; //u for upcoming, h for halfway only "for rounded trips", d for done , c for cancelled
    private String id;
    private String userId;
    private float speedSum;
    private int speedsCount;

    public Trip() {
        isRoundedTrip = false;
        status = "u";
        notes = new ArrayList<>();
        speedSum = 0;
        speedsCount = 0;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getTripRequestId() {
        return tripRequestId;
    }

    public void setTripRequestId(int tripRequestId) {
        this.tripRequestId = tripRequestId;
    }

    public String getTripName() {
        return tripName;
    }

    public void setTripName(String tripName) {
        this.tripName = tripName;
    }

    public TripDate getDate() {
        return date;
    }

    public void setDate(TripDate date) {
        this.date = date;
    }

    public boolean isRoundedTrip() {
        return isRoundedTrip;
    }

    public void setRoundedTrip(boolean roundedTrip) {
        isRoundedTrip = roundedTrip;
    }

    public TripLocation getEndPoint() {
        return endPoint;
    }

    public void setEndPoint(TripLocation endPoint) {
        this.endPoint = endPoint;
    }

    public TripLocation getStartPoint() {
        return startPoint;
    }

    public void setStartPoint(TripLocation startPoint) {
        this.startPoint = startPoint;
    }

    public TripTime getTime() {
        return time;
    }

    public void setTime(TripTime time) {
        this.time = time;
    }

    public void setIsRoundedTrip(boolean isRoundedTrip) {
        this.isRoundedTrip = isRoundedTrip;
    }

    public ArrayList<Note> getNotes() {
        return notes;
    }

    public void setNotes(ArrayList<Note> notes) {
        this.notes = notes;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public float getSpeedSum() {
        return speedSum;
    }

    public void setSpeedSum(float speedSum) {
        this.speedSum = speedSum;
    }

    public int getSpeedsCount() {
        return speedsCount;
    }

    public void setSpeedsCount(int speedsCount) {
        this.speedsCount = speedsCount;
    }
}
