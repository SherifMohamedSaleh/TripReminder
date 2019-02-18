package com.example.trip.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.trip.R;
import com.example.trip.adapters.RecyclerAdapter;
import com.example.trip.models.Trip;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {


    RecyclerView rv;
    List<Trip> tripList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        rv = findViewById(R.id.TripsRecyclerView);

        /*tripList.add(new Trip("trip one", " alex", " cairo", new Date(), 2));
        tripList.add(new Trip("trip two", " alex", " cairo", new Date(), 2));
        tripList.add(new Trip("trip three", " alex", " cairo", new Date(), 2));
        tripList.add(new Trip("trip four", " alex", " cairo", new Date(), 2));*/
        LinearLayoutManager llm = new LinearLayoutManager(this);
        rv.setLayoutManager(llm);

        RecyclerAdapter adapter = new RecyclerAdapter(tripList);
        rv.setAdapter(adapter);
    }

}
