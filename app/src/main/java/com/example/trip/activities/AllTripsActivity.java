package com.example.trip.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.example.trip.R;
import com.example.trip.adapters.RecyclerAdapter;
import com.example.trip.models.Note;
import com.example.trip.models.Trip;
import com.example.trip.models.TripDate;
import com.example.trip.models.TripLocation;
import com.example.trip.models.TripTime;
import com.example.trip.utils.FirebaseReferences;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import java.util.ArrayList;
import java.util.List;

public class AllTripsActivity extends AppCompatActivity implements FirebaseReferences {

    RecyclerView rv;
    FloatingActionButton addNewTripButton;
    List<Trip> tripDataList = new ArrayList<>();
    TripLocation start;
    TripLocation end;
    TripDate date;
    TripTime time;
    private ArrayList<Note> notes;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_trips);

        rv = (RecyclerView) findViewById(R.id.rv);
        addNewTripButton = findViewById(R.id.fab_add_trip);
        rv.setHasFixedSize(true);
        rv.setLayoutManager(new LinearLayoutManager(this));
        final RecyclerAdapter adapter = new RecyclerAdapter(this, tripDataList);
        rv.setAdapter(adapter);


        if (firebaseUser != null) {

            // DatabaseReference userRef=tripsRef.child(firebaseUser.getUid());
            Log.e("AllTripsActivity", "onCreate: " + firebaseUser.getUid());
            tripsRef.child(firebaseUser.getUid()).addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    Trip trip = new Trip();
                    String key = tripsRef.child(firebaseUser.getUid()).getKey();
                    trip = dataSnapshot.getValue(Trip.class);
                    if (trip != null) {

                        Log.e("AllTripsActivity", "onCreate: " + "add to list");

                        tripDataList.add(trip);
                        adapter.notifyDataSetChanged();

                        Log.e("AllTripsActivity", "onCreate: " + tripDataList.size());


                    } else {
                        Log.e("AllTripsActivity", "onCreate: " + "no current user");


                    }
                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {


                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                }

                @Override
                public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }

            });


        }
//        notes = new ArrayList<>();
//        start = new TripLocation();
//        end = new TripLocation();
//        date = new TripDate();
//        time = new TripTime();
//
//
//        tripDataList.add(new Trip(true, notes, "trip one", start, end, date, time));
//        tripDataList.add(new Trip(true, notes, "trip two", start, end, date, time));
//        tripDataList.add(new Trip(true, notes, "trip three", start, end, date, time));
//        tripDataList.add(new Trip(true, notes, "trip four", start, end, date, time));
//        tripDataList.add(new Trip(true, notes, "trip five", start, end, date, time));





        addNewTripButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AllTripsActivity.this, TempAddTripActivity.class));
            }
        });
    }

}
