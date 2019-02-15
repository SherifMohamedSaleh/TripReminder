package com.example.trip.activities;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.trip.R;
import com.example.trip.adapters.RecyclerAdapter;
import com.example.trip.models.TripData;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class AllTripsActivity extends AppCompatActivity {

    RecyclerView rv;
    List<TripData> tripDataList = new ArrayList<>();

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_trips);

        rv =(RecyclerView) findViewById(R.id.rv);
        rv.setHasFixedSize(true);
        rv.setLayoutManager(new LinearLayoutManager(this));



        tripDataList.add(new TripData("trip one", " alex", " cairo", 2    ));
        tripDataList.add(new TripData("trip two", " alex", " cairo", 2 ));
        tripDataList.add(new TripData("trip three", " alex", " cairo", 2 ));
        tripDataList.add(new TripData("trip four", " alex", " cairo",  2 ));


        RecyclerAdapter adapter = new RecyclerAdapter(this ,tripDataList);
        rv.setAdapter(adapter);
    }

}
