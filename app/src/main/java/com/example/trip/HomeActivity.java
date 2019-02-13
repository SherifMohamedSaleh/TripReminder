package com.example.trip;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    Toolbar toolbar;
    RecyclerView rv;
    List<TripData> tripDataList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        rv = (RecyclerView) findViewById(R.id.TripsRecyclerView);

        tripDataList.add(new TripData("trip one", " alex", " cairo", new Date(), 2));
        tripDataList.add(new TripData("trip two", " alex", " cairo", new Date(), 2));
        tripDataList.add(new TripData("trip three", " alex", " cairo", new Date(), 2));
        tripDataList.add(new TripData("trip four", " alex", " cairo", new Date(), 2));
        LinearLayoutManager llm = new LinearLayoutManager(toolbar.getContext());
        rv.setLayoutManager(llm);
        rv.setAdapter(new RecyclerAdapter(tripDataList));
    }

}
