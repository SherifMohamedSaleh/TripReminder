package com.example.trip.models;

import java.io.Serializable;

/**
 * Created by Toka on 2019-02-17.
 */
public class TripDate implements Serializable {
    private int day;
    private int month;
    private int year;

    public TripDate(int day, int month, int year) {
        this.day = day;
        this.month = month;
        this.year = year;
    }

    public TripDate() {
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }
}

