package com.example.trip.fragments;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.trip.R;
import com.example.trip.activities.HomeNavigationActivity;
import com.example.trip.adapters.RecyclerAdapter;
import com.example.trip.models.Trip;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import java.util.ArrayList;
import java.util.List;

import static com.example.trip.utils.FirebaseReferences.firebaseUser;
import static com.example.trip.utils.FirebaseReferences.tripsRef;

/**
 * A simple {@link Fragment} subclass.
 */
public class PastTripsFragment extends Fragment {
    RecyclerView rv;
    FloatingActionButton addNewTripButton;
    NavigationView navigationView;
    List<Trip> tripDataList = new ArrayList<>();

    public PastTripsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        navigationView = getActivity().findViewById(R.id.nav_view);
        View rootView=inflater.inflate(R.layout.fragment_past_trips, container, false);
        rv = (RecyclerView) rootView.findViewById(R.id.rv);
        rv.setHasFixedSize(true);
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        final RecyclerAdapter adapter = new RecyclerAdapter(getContext(), tripDataList);
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
                        if(trip.getStatus().equals("d")) {
                            tripDataList.add(trip);
                            adapter.notifyDataSetChanged();
                            Log.e("AllTripsActivity", "onCreate: " + tripDataList.size());
                        }

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
        return rootView;
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        FragmentManager fm=getFragmentManager();
        FragmentTransaction ft= fm.beginTransaction();
        navigationView.setCheckedItem(R.id.nav_home);
        ft.replace(R.id.fMain,new UpComingFragment());
        ft.commit();
    }
}
