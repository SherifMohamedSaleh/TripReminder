package com.example.trip.models;

import java.util.ArrayList;

public class Trip {
    private boolean isRoundedTrip;
    private ArrayList<String> notes;
    private String tripName;
    private TripLocation endPoint;
    private TripLocation startPoint;
    private TripDate date;
    private TripTime time;
    private boolean isFinished;

    public Trip() {
        isRoundedTrip = false;
        isFinished = false;

        notes = new ArrayList<>();
    }

    public boolean isFinished() {
        return isFinished;
    }

    public void setFinished(boolean finished) {
        isFinished = finished;
    }

    public Trip(boolean isRoundedTrip, ArrayList<String> notes, String tripName, TripLocation endPoint, TripLocation startPoint, TripDate date, TripTime time) {
        this.isRoundedTrip = isRoundedTrip;
        this.notes = notes;
        this.tripName = tripName;
        this.endPoint = endPoint;
        this.startPoint = startPoint;
        this.date = date;
        this.time = time;
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

    public ArrayList<String> getNotes() {
        return notes;
    }

    public void setNotes(ArrayList<String> notes) {
        this.notes = notes;
    }
}
