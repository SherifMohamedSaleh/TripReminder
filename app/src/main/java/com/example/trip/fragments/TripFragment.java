package com.example.trip.fragments;


import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.trip.R;
import com.example.trip.models.Trip;
import com.example.trip.models.TripDate;
import com.example.trip.models.TripLocation;
import com.example.trip.models.TripTime;
import com.example.trip.utils.FirebaseReferences;
import com.mapbox.api.geocoding.v5.models.CarmenFeature;
import com.mapbox.mapboxsdk.plugins.places.autocomplete.PlaceAutocomplete;
import com.mapbox.mapboxsdk.plugins.places.autocomplete.model.PlaceOptions;

import java.util.Calendar;

import static com.mapbox.mapboxsdk.plugins.places.autocomplete.model.PlaceOptions.MODE_CARDS;
/**
 * A simple {@link Fragment} subclass.
 */
public class TripFragment extends Fragment implements FirebaseReferences {

    final Calendar calender = Calendar.getInstance();
    final int hour = calender.get(Calendar.HOUR_OF_DAY);
    final int minute = calender.get(Calendar.MINUTE);
    final int year = calender.get(Calendar.YEAR);
    final int month = calender.get(Calendar.MONTH);
    final int day = calender.get(Calendar.DAY_OF_MONTH);
    private static final String MAPBOX_ACCESS_TOKEN = "sk.eyJ1IjoidG9rYWFsaWFtaW4iLCJhIjoiY2pzODBzcjlrMTJ4azN5bnV6a3E2cTJiaSJ9.jWdMw48rKqQ9t-cd8J0KBA";
    private static final int REQUEST_CODE_START_AUTOCOMPLETE = 1;
    private static final int REQUEST_CODE_END_AUTOCOMPLETE = 2;

    EditText tripName, tripDate, tripTime, tripSource, tripDest;
    FloatingActionButton editTrip, startTrip, saveTrip;
    Button notesBtn, roundedBtn;
    NavigationView navigationView;
    TextView tripStatus;
    CheckBox doneCheckBox;
    ImageView tripImage;
    Boolean editMode = false;
    Drawable draw;
    View view;
    Trip trip;

    public TripFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_trip, container, false);
        navigationView = getActivity().findViewById(R.id.nav_view);
        tripName = view.findViewById(R.id.tripNameVal);
        tripStatus = view.findViewById(R.id.tripStatusVal);
        tripDate = view.findViewById(R.id.tripDateVal);
        tripTime = view.findViewById(R.id.tripTimeVal);
        tripSource = view.findViewById(R.id.tripSourceVal);
        tripDest = view.findViewById(R.id.tripDestVal);
        doneCheckBox = view.findViewById(R.id.doneCheckBox);
        notesBtn = view.findViewById(R.id.notesBtn);
        roundedBtn = view.findViewById(R.id.startRoundBtn);
        editTrip = view.findViewById(R.id.editTripBtn);
        saveTrip = view.findViewById(R.id.saveTripBtn);
        startTrip = view.findViewById(R.id.startTripBtn);
        draw = tripName.getBackground();
        tripName.setText(trip.getTripName());

        if (trip.getStatus().equals("d")) {
            tripStatus.setText("Completed");
            doneCheckBox.setChecked(true);
        } else if (trip.getStatus().equals("u")) {
            tripStatus.setText("Upcoming");
            doneCheckBox.setChecked(false);
        } else if (trip.getStatus().equals("c")) {
            tripStatus.setText("Cancelled");
            doneCheckBox.setChecked(false);
        }
        if (trip.isRoundedTrip()) {
            roundedBtn.setVisibility(View.VISIBLE);
        }
        tripDate.setText(Integer.toString(trip.getDate().getDay()) + "/" + Integer.toString(trip.getDate().getMonth()) + "/" + Integer.toString(trip.getDate().getYear()));
        tripTime.setText(Integer.toString(trip.getTime().getHour()) + ":" + Integer.toString(trip.getTime().getMinute()));
        tripSource.setText("• " + trip.getStartPoint().getAddress());
        tripDest.setText("• " + trip.getEndPoint().getAddress());

        editTrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editMode = true;
                saveTrip.setVisibility(View.VISIBLE);
                editTrip.setVisibility(View.INVISIBLE);
                tripName.setFocusable(true);
                tripName.setClickable(true);
                tripName.setFocusableInTouchMode(true);
                tripName.setBackgroundDrawable(draw);
                tripDate.setFocusable(true);
                tripDate.setClickable(true);
                tripDate.setFocusableInTouchMode(true);
                tripDate.setBackgroundDrawable(draw);
                tripTime.setFocusable(true);
                tripTime.setClickable(true);
                tripTime.setFocusableInTouchMode(true);
                tripTime.setBackgroundDrawable(draw);
                tripSource.setFocusable(true);
                tripSource.setClickable(true);
                tripSource.setFocusableInTouchMode(true);
                tripSource.setBackgroundDrawable(draw);
                tripDest.setClickable(true);
                tripDest.setFocusable(true);
                tripDest.setFocusableInTouchMode(true);
                tripDest.setBackgroundDrawable(draw);
            }
        });
        if (editMode) {
            startTrip.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker timePicker, int selectedHours, int selectedMinute) {
                            trip.setTime(new TripTime(selectedHours, selectedMinute));
                            tripTime.setText(Integer.toString(trip.getTime().getHour()) + ":" + Integer.toString(trip.getTime().getMinute()));
                        }

                    }, hour, minute, false);

                    timePickerDialog.show();
                }
            });

            tripDate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.i("Date", "onClick: ");
                    DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {

                        @Override
                        public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                            trip.setDate(new TripDate(day, month, year));
                            tripDate.setText(Integer.toString(trip.getDate().getDay()) + "/" + Integer.toString(trip.getDate().getMonth()) + "/" + Integer.toString(trip.getDate().getYear()));
                        }
                    }, year, month, day);
                    datePickerDialog.show();
                }
            });
            tripSource.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new PlaceAutocomplete.IntentBuilder()
                            .accessToken(MAPBOX_ACCESS_TOKEN)
                            .placeOptions(PlaceOptions.builder().build(MODE_CARDS))
                            .build(getActivity());
                    startActivityForResult(intent, REQUEST_CODE_START_AUTOCOMPLETE);
                }
            });
            tripDest.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new PlaceAutocomplete.IntentBuilder()
                            .accessToken(MAPBOX_ACCESS_TOKEN)
                            .placeOptions(PlaceOptions.builder().build(MODE_CARDS))
                            .build(getActivity());
                    startActivityForResult(intent, REQUEST_CODE_END_AUTOCOMPLETE);
                }
            });
        } else {
            tripName.setFocusable(false);
            tripName.setClickable(false);
            tripName.setFocusableInTouchMode(false);
            tripName.setBackground(null);
            tripDate.setFocusable(false);
            tripDate.setClickable(false);
            tripDate.setBackground(null);
            tripDate.setFocusableInTouchMode(false);
            tripTime.setFocusable(false);
            tripTime.setClickable(false);
            tripTime.setBackground(null);
            tripTime.setFocusableInTouchMode(false);
            tripSource.setFocusable(false);
            tripSource.setFocusable(false);
            tripSource.setBackground(null);
            tripSource.setFocusableInTouchMode(false);
            tripDest.setClickable(false);
            tripDest.setFocusable(false);
            tripDest.setFocusableInTouchMode(false);
            tripDest.setBackground(null);
            doneCheckBox.setFocusable(false);
            doneCheckBox.setClickable(false);
        }
        saveTrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editMode = false;
                saveTrip.setVisibility(View.INVISIBLE);
                editTrip.setVisibility(View.VISIBLE);
                tripName.setFocusable(false);
                tripName.setClickable(false);
                tripName.setFocusableInTouchMode(false);
                tripName.setBackground(null);
                tripDate.setFocusable(false);
                tripDate.setClickable(false);
                tripDate.setBackground(null);
                tripDate.setFocusableInTouchMode(false);
                tripTime.setFocusable(false);
                tripTime.setClickable(false);
                tripTime.setFocusableInTouchMode(false);
                tripTime.setBackground(null);
                tripSource.setFocusable(false);
                tripSource.setFocusable(false);
                tripSource.setFocusableInTouchMode(false);
                tripSource.setBackground(null);
                tripDest.setClickable(false);
                tripDest.setFocusable(false);
                tripDest.setFocusableInTouchMode(false);
                tripDest.setBackground(null);
                doneCheckBox.setFocusable(false);
                doneCheckBox.setClickable(false);
                trip.setTripName(tripName.getText().toString());
                tripsRef.child(firebaseUser.getUid()).child(trip.getId()).setValue(trip);
            }
        });
        notesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NoteFragment noteFragment = new NoteFragment();
                noteFragment.setTrip(trip);
                FragmentManager fm = getFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.addToBackStack(null);
                ft.replace(R.id.fMain, noteFragment, "tripFragmentTag");
                ft.commit();
            }
        });

        return view;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE_START_AUTOCOMPLETE) {
            CarmenFeature feature = PlaceAutocomplete.getPlace(data);
            tripSource.setText(feature.text());
            trip.setStartPoint(new TripLocation(feature.center().latitude(), feature.center().longitude(), feature.text()));
        } else if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE_END_AUTOCOMPLETE) {
            CarmenFeature feature = PlaceAutocomplete.getPlace(data);
            tripDest.setText(feature.text());
            trip.setEndPoint(new TripLocation(feature.center().latitude(), feature.center().longitude(), feature.text()));
        }
    }

    public void setTrip(Trip trip) {
        this.trip = trip;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.fMain, new UpComingFragment());
        navigationView.setCheckedItem(R.id.nav_home);
        ft.commit();
    }
}

