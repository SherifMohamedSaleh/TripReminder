package com.example.trip.models;

/**
 * Created by Toka on 2019-02-17.
 */
public class TripTime {
    private int hour;
    private int minute;

    public TripTime() {

    }

    public TripTime(int hour, int minute) {

        this.hour = hour;
        this.minute = minute;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }
}