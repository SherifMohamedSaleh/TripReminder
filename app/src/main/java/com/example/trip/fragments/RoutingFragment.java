package com.example.trip.fragments;

import android.app.Dialog;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

import com.example.trip.R;
import com.example.trip.adapters.NotesAdapter;
import com.example.trip.models.Trip;
import com.example.trip.models.TripLocation;
import com.example.trip.utils.FirebaseReferences;
import com.mapbox.android.core.permissions.PermissionsListener;
import com.mapbox.android.core.permissions.PermissionsManager;
import com.mapbox.api.directions.v5.models.DirectionsResponse;
import com.mapbox.api.directions.v5.models.DirectionsRoute;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.location.LocationComponent;
import com.mapbox.mapboxsdk.location.modes.CameraMode;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.services.android.navigation.ui.v5.NavigationView;
import com.mapbox.services.android.navigation.ui.v5.NavigationViewOptions;
import com.mapbox.services.android.navigation.ui.v5.OnNavigationReadyCallback;
import com.mapbox.services.android.navigation.ui.v5.listeners.NavigationListener;
import com.mapbox.services.android.navigation.ui.v5.route.NavigationMapRoute;
import com.mapbox.services.android.navigation.v5.navigation.NavigationRoute;
import com.mapbox.services.android.navigation.v5.routeprogress.ProgressChangeListener;
import com.mapbox.services.android.navigation.v5.routeprogress.RouteProgress;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

// classes needed to launch navigation UI

// classes to calculate a route

public class RoutingFragment extends Fragment implements OnNavigationReadyCallback, NavigationListener, PermissionsListener, ProgressChangeListener, FirebaseReferences {
    private static final String MAPBOX_ACCESS_TOKEN = "sk.eyJ1IjoidG9rYWFsaWFtaW4iLCJhIjoiY2pzODBzcjlrMTJ4azN5bnV6a3E2cTJiaSJ9.jWdMw48rKqQ9t-cd8J0KBA";
    private static final String TAG = "DirectionsActivity";
    DirectionsRoute directionsRoute;
    private MapboxMap mapboxMap;
    private PermissionsManager permissionsManager;
    private LocationComponent locationComponent;
    // variables for calculating and drawing a route
    private DirectionsRoute currentRoute;
    private NavigationMapRoute navigationMapRoute;
    // variables needed to initialize navigation
    private FloatingActionButton button;
    Trip trip;
    private NavigationView navigationView;

    private boolean arrived;
    private boolean roundFinished;
    private boolean isHeadingBack;

    private ArrayList<Float> tripSpeeds;


    public RoutingFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Mapbox.getInstance(getContext(), MAPBOX_ACCESS_TOKEN);
        View rootView = inflater.inflate(R.layout.fragment_routing, container, false);
        /*lat1 = 31.2111877; //29.91667
        lng1 = 29.9245365; //31.2
        lat2 = 31.210574; //31.23944
        lng2 = 29.924665; //30.05611
        destinationPoint = Point.fromLngLat(lng2, lat2);
        originPoint = Point.fromLngLat(lng1, lat1);*/

        navigationView = rootView.findViewById(R.id.navigation_view);
        RecyclerView notesRecyclerView = rootView.findViewById(R.id.rv_routing_notes);

        arrived = false;
        roundFinished = false;
        isHeadingBack = false;
        tripSpeeds = new ArrayList<>();

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            trip = (Trip) bundle.getSerializable("trip");
            if (trip != null) {
                if (trip.getNotes() != null) {
                    NotesAdapter notesAdapter = new NotesAdapter(trip.getNotes(), getContext());
                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
                    notesRecyclerView.setAdapter(notesAdapter);
                    notesRecyclerView.setLayoutManager(linearLayoutManager);
                }
                navigationView.onCreate(savedInstanceState);
                navigationView.initialize(this);
            }
        }
        return rootView;
    }

    @SuppressWarnings({"MissingPermission"})
    private void enableLocationComponent(@NonNull Style loadedMapStyle) {
        // Check if permissions are enabled and if not request
        if (PermissionsManager.areLocationPermissionsGranted(getContext())) {
            // Activate the MapboxMap LocationComponent to show user location
            // Adding in LocationComponentOptions is also an optional parameter
            locationComponent = mapboxMap.getLocationComponent();
            locationComponent.activateLocationComponent(getContext(), loadedMapStyle);
            locationComponent.setLocationComponentEnabled(true);
            // Set the component's camera mode
            locationComponent.setCameraMode(CameraMode.TRACKING);
        } else {
            permissionsManager = new PermissionsManager(this);
            permissionsManager.requestLocationPermissions(getActivity());
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        permissionsManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onExplanationNeeded(List<String> permissionsToExplain) {
        Toast.makeText(getActivity(), "premission", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onPermissionResult(boolean granted) {
        if (granted) {
            enableLocationComponent(mapboxMap.getStyle());
        } else {
            Toast.makeText(getActivity(), "premission", Toast.LENGTH_LONG).show();
            getActivity().finish();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        navigationView.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        navigationView.onResume();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        navigationView.onSaveInstanceState(outState);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (savedInstanceState != null) {
            navigationView.onRestoreInstanceState(savedInstanceState);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        navigationView.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        navigationView.onStop();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        navigationView.onLowMemory();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        navigationView.onDestroy();
    }

    @Override
    public void onNavigationReady(boolean isRunning) {
        getRoute(trip.getStartPoint(), trip.getEndPoint());
    }


    @Override
    public void onCancelNavigation() {
        navigationView.stopNavigation();
        //stopNavigation();
    }

    @Override
    public void onNavigationFinished() {
// no-op
        //Toast.makeText(getContext(), "Arriveddd", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNavigationRunning() {
// no-op
    }


    private void getRoute(TripLocation origin, TripLocation destination) {
        Point destinationPoint = Point.fromLngLat(destination.getLng(), destination.getLat());
        Point originPoint = Point.fromLngLat(origin.getLng(), origin.getLat());
        if (Mapbox.getAccessToken() != null) {
            NavigationRoute.builder(getContext())
                    .accessToken(Mapbox.getAccessToken())
                    .origin(originPoint)
                    .destination(destinationPoint)
                    .build()
                    .getRoute(new Callback<DirectionsResponse>() {
                        @Override
                        public void onResponse(Call<DirectionsResponse> call, Response<DirectionsResponse> response) {
                            if (response.body() != null && response.body().routes().size() > 0) {
                                directionsRoute = response.body().routes().get(0);
                                startNavigation();
                            }
                        }

                        @Override
                        public void onFailure(Call<DirectionsResponse> call, Throwable throwable) {
                            Log.e(TAG, "Error: " + throwable.getMessage());
                        }
                    });
        }
    }

    private void startNavigation() {
        if (directionsRoute == null) {
            return;
        }
        //TODO start navigation from start and not from current location
        NavigationViewOptions options = NavigationViewOptions.builder()
                .directionsRoute(directionsRoute)
                .shouldSimulateRoute(true)
                .navigationListener(RoutingFragment.this)
                .progressChangeListener(this)
                .build();
        navigationView.startNavigation(options);
    }


    @Override
    public void onProgressChange(Location location, RouteProgress routeProgress) {
        //TODO handel it
        Log.i(TAG, "destance left" + routeProgress.fractionTraveled());
        if (!arrived || !trip.getStatus().equals("d")) {
            if (routeProgress.fractionTraveled() > 0.95) {

                arrived = true;
                final Dialog dialog = new Dialog(getActivity());
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setCancelable(false);

                if (trip.isRoundedTrip() && trip.getStatus().equals("u")) {
                    Log.i(TAG, "trip.isRoundedTrip() && trip.getStatus().equals(\"u\")");
                    dialog.setContentView(R.layout.dialog_round_trip);

                    Button letsGoButton = dialog.findViewById(R.id.btn_lets_go);
                    Button laterButton = dialog.findViewById(R.id.btn_later);

                    letsGoButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                            setTripStatus("h");
                            arrived = false;
                            getRoute(trip.getEndPoint(), trip.getStartPoint());
                        }
                    });

                    laterButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialog.setContentView(R.layout.dialog_round_trip_pickers);
                            //TODO add to firebase and to the variable in the list
                            setTripStatus("h");
                            dialog.dismiss();
                            FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                            ft.replace(R.id.fMain, new UpComingFragment());
                            ft.addToBackStack(null);
                            ft.commit();

                        }
                    });

                    dialog.show();
                } else if (!trip.isRoundedTrip() || trip.isRoundedTrip() && trip.getStatus().equals("h")) {
                    Log.i(TAG, "!trip.isRoundedTrip() || trip.isRoundedTrip() && trip.getStatus().equals(\"h\")");
                    setTripStatus("d");
                    dialog.setContentView(R.layout.dialog_trip_finished);
                    Button goToHomeButton = dialog.findViewById(R.id.btn_go_to_home_screen);
                    goToHomeButton.setOnClickListener(view -> {
                        dialog.dismiss();
                        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                        ft.replace(R.id.fMain, new UpComingFragment());
                        ft.addToBackStack(null);
                        ft.commit();
                    });

                    dialog.show();
                }
                //calculate trip speed
                float speedSum = trip.getSpeedSum();
                if (tripSpeeds.size() > 0) {
                    for (float speed : tripSpeeds) {
                        speedSum += speed;
                    }
                }
                if (firebaseUser != null) {
                    tripsRef.child(firebaseUser.getUid()).child(trip.getId()).child("speedSum").setValue(speedSum);
                    tripsRef.child(firebaseUser.getUid()).child(trip.getId()).child("speedsCount").setValue(trip.getSpeedsCount() + tripSpeeds.size());
                }

            } else 
                float speed = location.getSpeed();
                if (speed != 0) {
                    //TODO add to firebase and to the variable in the list
                    tripSpeeds.add(speed);
                }
            }
        }

    }

    private void setTripStatus(String newStatus) {
        if (firebaseUser != null) {
            trip.setStatus(newStatus);
            tripsRef.child(firebaseUser.getUid()).child(trip.getId()).child("status").setValue(newStatus);
        }
    }
}
