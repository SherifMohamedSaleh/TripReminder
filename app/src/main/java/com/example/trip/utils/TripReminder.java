package com.example.trip.utils;

import android.app.Application;

import com.google.firebase.database.FirebaseDatabase;

public class TripReminder extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }
}