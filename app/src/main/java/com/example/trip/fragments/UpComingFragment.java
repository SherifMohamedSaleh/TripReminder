package com.example.trip.fragments;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.opengl.EGLExt;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.trip.R;
import com.example.trip.activities.MainActivity;
import com.example.trip.adapters.RecyclerAdapter;
import com.example.trip.models.Trip;
import com.example.trip.utils.FirebaseReferences;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import static android.support.v4.content.ContextCompat.getSystemService;

public class UpComingFragment extends Fragment implements FirebaseReferences {


    RecyclerView rv;
    FloatingActionButton addNewTripButton;
    ProgressBar progressBar;
    ConstraintLayout noTripsLayout;
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

    List<Trip> tripDataList = new ArrayList<>();
    CoordinatorLayout coordinatorLayout;

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
        Animation shake = (Animation) AnimationUtils.loadAnimation(getContext(), R.anim.shake);


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
  //                      Log.i("firebase id", firebaseUser.getUid());
//                        Log.i("trip firebase id", trip.getUserId());
                        if (trip != null && trip.getUserId().equals(firebaseUser.getUid())) {
                            if (trip.getStatus().equals("u")) {
                                tripDataList.add(trip);
                                adapter.notifyDataSetChanged();
                            }
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
                boolean isConected = isNetworkAvailable();
                if(isConected == true) {
                    FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.fMain, new AddTripFragment());
                    ft.addToBackStack(null);
                    ft.commit();
                }
                else{
                    addNewTripButton.startAnimation(shake);
                    addNewTripButton.requestFocus();
                    View v = getActivity().findViewById(android.R.id.content);
                    Snackbar.make(v, "you can't add trip when you are offline", Snackbar.LENGTH_LONG).show();
//                    Toast.makeText(getActivity(), "you can't add trip when you are offline",
//                            Toast.LENGTH_LONG).show();

                }
            }
        });
        return rootView;
    }
    private boolean isNetworkAvailable() {
        ConnectivityManager manager =
                (ConnectivityManager)getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        boolean isAvailable = false;
        if (networkInfo != null && networkInfo.isConnected()) {
            // Network is present and connected
            isAvailable = true;
        }
        return isAvailable;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        tripDataList.clear();
    }
}



