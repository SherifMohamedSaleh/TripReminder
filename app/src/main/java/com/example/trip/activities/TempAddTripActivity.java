package com.example.trip.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.example.trip.R;
import com.example.trip.fragments.AddTripFragment;

public class TempAddTripActivity extends AppCompatActivity {

    private static final String TAG = "TempAddTripActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_temp_nav);
        AddTripFragment fragment = new AddTripFragment();
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.add(R.id.activity_main, fragment);
        transaction.commit();
    }

}