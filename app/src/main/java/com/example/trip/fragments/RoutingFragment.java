package com.example.trip.fragments;

import android.Manifest;
import android.app.Dialog;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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
    private static final int ACCESS_FINE_LOCATION_REQUEST_CODE = 1;
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
    private ConstraintLayout noNotesLayout;
    Trip trip;
    private NavigationView navigationView;
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
    private boolean arrived;
    private boolean roundFinished;
    private boolean isHeadingBack;

    private ArrayList<Float> tripSpeeds;

    RecyclerView notesRecyclerView;

    Bundle savedInstanceState;

    View rootView;
    private boolean mLocationPermissionGranted = false;


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
        rootView = inflater.inflate(R.layout.fragment_routing, container, false);
        navigationView = rootView.findViewById(R.id.navigation_view);
        notesRecyclerView = rootView.findViewById(R.id.rv_routing_notes);
        noNotesLayout = rootView.findViewById(R.id.layout_no_notes);

        arrived = false;
        roundFinished = false;
        isHeadingBack = false;
        tripSpeeds = new ArrayList<>();

        this.savedInstanceState = savedInstanceState;

        getLocationPermission();
        return rootView;
    }


    private void startTrip() {
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            trip = (Trip) bundle.getSerializable("trip");
            if (trip != null) {
                if (trip.getNotes().size() > 0) {
                    Log.e(TAG, "notes exists");
                    noNotesLayout.setVisibility(View.INVISIBLE);
                    NotesAdapter notesAdapter = new NotesAdapter(trip.getNotes(), getContext());
                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
                    notesRecyclerView.setAdapter(notesAdapter);
                    notesRecyclerView.setLayoutManager(linearLayoutManager);
                } else {
                    noNotesLayout.setVisibility(View.VISIBLE);
                    Log.e(TAG, "notes doesn't exist");

                }
                navigationView.onCreate(savedInstanceState);
                navigationView.initialize(this);
            }
        }
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
        if (mLocationPermissionGranted)
            getRoute(trip.getStartPoint(), trip.getEndPoint());
        else {
            if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
        }
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
                            if (response.body() != null && response.body().routes().size() > 0 && response.body().routes().get(0).distance() != 0) {
                                directionsRoute = response.body().routes().get(0);
                                startNavigation();
                            } else {
                                Snackbar.make(rootView, "Couldn't find route", Snackbar.LENGTH_SHORT).show();
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
        if (routeProgress.fractionTraveled() > 0.96 && !arrived) {
            arrived = true;
            if (!trip.isRoundedTrip()) {
                Log.i(TAG, "!trip.isRoundedTrip()&&!arrived");
                showGoToHomeDialog();
                setTripStatus("d");
            } else if (trip.isRoundedTrip() && trip.getStatus().equals("u")) {
                Log.i(TAG, "trip.isRoundedTrip() && trip.getStatus().equals(\"u\")");
                showOptionsDialog();
            } else if (trip.isRoundedTrip() && trip.getStatus().equals("h")) {
                //only half the trip is finished
                Log.i(TAG, "trip.isRoundedTrip() && trip.getStatus().equals(\"h\")");
                arrived = false;
                setTripStatus("d");
                getRoute(trip.getEndPoint(), trip.getStartPoint());
            } else if (trip.isRoundedTrip() && trip.getStatus().equals("d")) {
                Log.i(TAG, "trip.isRoundedTrip() && trip.getStatus().equals(\"d\")");
                showGoToHomeDialog();
            }

        }
    }

    private void showOptionsDialog() {
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);

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
    }

    private void showGoToHomeDialog() {
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
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

    private void setTripStatus(String newStatus) {
        if (firebaseUser != null) {
            trip.setStatus(newStatus);
            tripsRef.child(firebaseUser.getUid()).child(trip.getId()).child("status").setValue(newStatus);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fMain, new UpComingFragment());
        ft.addToBackStack(null);
        ft.commit();
    }

    private void getLocationPermission() {
        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION};
        Log.e(TAG, "getLocationPermission: getting location permission");

        //now we want to check whether or not permission is granted

        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
            startTrip();

        } else {
            requestPermissions(permissions, ACCESS_FINE_LOCATION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.e(TAG, "onRequestPermissionsResult: Called!.");
        mLocationPermissionGranted = false;
        if (requestCode == ACCESS_FINE_LOCATION_REQUEST_CODE) {
            if (grantResults.length > 0) {
                for (int i = 0; i < grantResults.length; i++) {
                    if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                        mLocationPermissionGranted = false;
                        Toast.makeText(getContext(), "We need location permission", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                Log.e(TAG, "onRequestPermissionsResult: permission granted");
                mLocationPermissionGranted = true;
                //showSettingsAlert();
                //initialize our map
                startTrip();
            }
        }
    }
}



