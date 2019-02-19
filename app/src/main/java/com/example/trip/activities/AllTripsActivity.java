package com.example.trip.activities;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.trip.R;
import com.example.trip.adapters.RecyclerAdapter;
import com.example.trip.models.Note;
import com.example.trip.models.Trip;
import com.example.trip.models.TripDate;
import com.example.trip.models.TripLocation;
import com.example.trip.models.TripTime;

import java.util.ArrayList;
import java.util.List;

public class AllTripsActivity extends AppCompatActivity {

    RecyclerView rv;
    FloatingActionButton addNewTripButton;
    List<Trip> tripDataList = new ArrayList<>();
    TripLocation start;
    TripLocation end;
    TripDate date;
    TripTime time;
    private ArrayList<Note> notes;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_trips);

        rv = (RecyclerView) findViewById(R.id.rv);
        addNewTripButton = findViewById(R.id.fab_add_trip);
        rv.setHasFixedSize(true);
        rv.setLayoutManager(new LinearLayoutManager(this));
        notes = new ArrayList<>();
        start = new TripLocation();
        end = new TripLocation();
        date = new TripDate();
        time = new TripTime();


        tripDataList.add(new Trip(true, notes, "trip one", start, end, date, time));
        tripDataList.add(new Trip(true, notes, "trip two", start, end, date, time));
        tripDataList.add(new Trip(true, notes, "trip three", start, end, date, time));
        tripDataList.add(new Trip(true, notes, "trip four", start, end, date, time));
        tripDataList.add(new Trip(true, notes, "trip five", start, end, date, time));


        RecyclerAdapter adapter = new RecyclerAdapter(this, tripDataList);
        rv.setAdapter(adapter);

        addNewTripButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AllTripsActivity.this, TempAddTripActivity.class));
            }
        });
    }

}
