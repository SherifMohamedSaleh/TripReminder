package com.example.trip.fragments;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static com.example.trip.utils.FirebaseReferences.tripsRef;

public class PastTripsFragment extends Fragment {
    RecyclerView rv;
    FloatingActionButton viewOnMapButton;
    NavigationView navigationView;
    List<Trip> tripDataList = new ArrayList<>();
    ProgressBar progressBar;
    ConstraintLayout noTripsLayout;
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
    public PastTripsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        navigationView = getActivity().findViewById(R.id.nav_view);
        View rootView = inflater.inflate(R.layout.fragment_past_trips, container, false);
        viewOnMapButton = rootView.findViewById(R.id.fab_view_on_map);
        rv = rootView.findViewById(R.id.rv);
        progressBar = rootView.findViewById(R.id.progress_bar);
        noTripsLayout = rootView.findViewById(R.id.layout_no_trips_history);

        progressBar.setVisibility(View.VISIBLE);
        noTripsLayout.setVisibility(View.INVISIBLE);

        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        final RecyclerAdapter adapter = new RecyclerAdapter(getContext(), tripDataList);
        rv.setAdapter(adapter);
        if (firebaseUser != null) {
            tripsRef.keepSynced(true);
            // DatabaseReference userRef=tripsRef.child(firebaseUser.getUid());
            Log.e("AllTripsActivity", "onCreate: " + firebaseUser.getUid());
            tripsRef.child(firebaseUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    tripDataList.clear();
                    progressBar.setVisibility(View.INVISIBLE);
                    for (DataSnapshot tripSnapShot : dataSnapshot.getChildren()) {
                        Trip trip = tripSnapShot.getValue(Trip.class);
                        if (trip != null) {
                            Log.e("AllTripsActivity", "onCreate: " + "add to list");
                            if (trip.getStatus().equals("d")) {
                                tripDataList.add(trip);
                                adapter.notifyDataSetChanged();
                                Log.e("AllTripsActivity", "onCreate: " + tripDataList.size());
                            }

                        } else {
                            Log.e("AllTripsActivity", "onCreate: " + "no current user");

                        }

                    }
                    if (tripDataList.size() <= 0) {
                        noTripsLayout.setVisibility(View.VISIBLE);
                        viewOnMapButton.hide();
                    } else {
                        noTripsLayout.setVisibility(View.INVISIBLE);
                        viewOnMapButton.show();

                        viewOnMapButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Log.i("Past trips", "map button clicked");
                                Bundle bundle = new Bundle();
                                bundle.putSerializable("trips", (Serializable) tripDataList);
                                StaticMapFragment fragment = new StaticMapFragment();
                                fragment.setArguments(bundle);
                                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                                ft.addToBackStack(null);
                                ft.replace(R.id.fMain, fragment);
                                ft.commit();
                            }
                        });
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }

        return rootView;
    }
}
