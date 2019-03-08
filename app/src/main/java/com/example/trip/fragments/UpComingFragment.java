package com.example.trip.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.example.trip.R;
import com.example.trip.adapters.RecyclerAdapter;
import com.example.trip.models.Trip;
import com.example.trip.utils.FirebaseReferences;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class UpComingFragment extends Fragment implements FirebaseReferences {


    RecyclerView rv;
    FloatingActionButton addNewTripButton;
    ProgressBar progressBar;
    ConstraintLayout noTripsLayout;


    List<Trip> tripDataList = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_up_coming, container, false);

        rv = (RecyclerView) rootView.findViewById(R.id.rv);
        addNewTripButton = rootView.findViewById(R.id.fab_add_trip);
        progressBar = rootView.findViewById(R.id.progress_bar);
        noTripsLayout = rootView.findViewById(R.id.layout_no_trips_upcoming);

        progressBar.setVisibility(View.VISIBLE);
        noTripsLayout.setVisibility(View.INVISIBLE);

        rv.setHasFixedSize(true);
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        final RecyclerAdapter adapter = new RecyclerAdapter(getContext(), tripDataList);
        rv.setAdapter(adapter);


        if (firebaseUser != null) {
            tripsRef.keepSynced(true);
            Log.e("AllTripsActivity", "onCreate: " + firebaseUser.getUid());

            tripsRef.child(firebaseUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    progressBar.setVisibility(View.INVISIBLE);
                    for (DataSnapshot tripSnapShot : dataSnapshot.getChildren()) {
                        Trip trip = tripSnapShot.getValue(Trip.class);
                        if (trip != null) {
                            tripDataList.add(trip);
                            adapter.notifyDataSetChanged();
                        }
                    }

                    if (tripDataList.size() <= 0)
                        noTripsLayout.setVisibility(View.VISIBLE);
                    else
                        noTripsLayout.setVisibility(View.INVISIBLE);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    progressBar.setVisibility(View.VISIBLE);
                    noTripsLayout.setVisibility(View.INVISIBLE);
                }
            });
        }
        addNewTripButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.fMain, new AddTripFragment());
                ft.addToBackStack(null);
                ft.commit();
            }
        });
        return rootView;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        tripDataList.clear();
    }
}



