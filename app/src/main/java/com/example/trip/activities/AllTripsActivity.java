package com.example.trip.activities;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.trip.R;
import com.example.trip.adapters.RecyclerAdapter;
import com.example.trip.models.Trip;
import com.example.trip.models.TripDate;
import com.example.trip.models.TripLocation;
import com.example.trip.models.TripTime;


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class AllTripsActivity extends AppCompatActivity {

    RecyclerView rv;
    List<Trip> tripDataList = new ArrayList<>();
    private ArrayList<String> notes;
    TripLocation start ;
    TripLocation end ;
    TripDate date ;
    TripTime time;


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_trips);

        rv =(RecyclerView) findViewById(R.id.rv);
        rv.setHasFixedSize(true);
        rv.setLayoutManager(new LinearLayoutManager(this));
        notes = new ArrayList<>();
        start = new TripLocation();
        end = new TripLocation();
        date = new TripDate();
        time = new TripTime();



        tripDataList.add(new Trip( true ,notes,"trip one", start , end ,date , time  ));
        tripDataList.add(new Trip( true ,notes,"trip two", start , end ,date , time ));
        tripDataList.add(new Trip( true ,notes,"trip three", start , end ,date , time ));
        tripDataList.add(new Trip( true ,notes,"trip four", start , end ,date , time));
        tripDataList.add(new Trip( true ,notes,"trip five", start , end ,date , time));


        RecyclerAdapter adapter = new RecyclerAdapter(this ,tripDataList);
        rv.setAdapter(adapter);
    }

}
