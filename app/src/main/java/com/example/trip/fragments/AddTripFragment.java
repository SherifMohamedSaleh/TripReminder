package com.example.trip.fragments;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.trip.R;
import com.example.trip.adapters.AddNotesAdapter;
import com.mapbox.api.geocoding.v5.models.CarmenFeature;
import com.mapbox.mapboxsdk.plugins.places.autocomplete.PlaceAutocomplete;
import com.mapbox.mapboxsdk.plugins.places.autocomplete.model.PlaceOptions;

import java.util.ArrayList;
import java.util.Calendar;

import static com.mapbox.mapboxsdk.plugins.places.autocomplete.model.PlaceOptions.MODE_CARDS;

public class AddTripFragment extends Fragment {
    private static final String TAG = "AddTripFragment";
    private static final String MAPBOX_ACCESS_TOKEN = "sk.eyJ1IjoidG9rYWFsaWFtaW4iLCJhIjoiY2pzODBzcjlrMTJ4azN5bnV6a3E2cTJiaSJ9.jWdMw48rKqQ9t-cd8J0KBA";
    private static final int REQUEST_CODE_START_AUTOCOMPLETE = 1;
    private static final int REQUEST_CODE_END_AUTOCOMPLETE = 2;

    TextInputEditText tripNameEditText;
    ImageButton timeButton, dateButton, addNoteButton, addStartButton, addEndButton;
    RecyclerView notesRecyclerView;
    TextView startPointTextView, endPointTextView;

    ArrayList<String> notesArrayList;
    AddNotesAdapter addNotesAdapter;
    LinearLayoutManager linearLayoutManager;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_add_trip, container, false);

        tripNameEditText = rootView.findViewById(R.id.et_trip_name);
        timeButton = rootView.findViewById(R.id.btn_trip_time);
        dateButton = rootView.findViewById(R.id.btn_trip_date);
        addNoteButton = rootView.findViewById(R.id.btn_add_note);
        addStartButton = rootView.findViewById(R.id.btn_start_point);
        addEndButton = rootView.findViewById(R.id.btn_end_point);
        notesRecyclerView = rootView.findViewById(R.id.rv_notes);
        startPointTextView = rootView.findViewById(R.id.tv_start_point);
        endPointTextView = rootView.findViewById(R.id.tv_end_point);

        notesArrayList = new ArrayList<>();
        addNotesAdapter = new AddNotesAdapter(notesArrayList, getContext());

        linearLayoutManager = new LinearLayoutManager(getContext());
        notesRecyclerView.setAdapter(addNotesAdapter);
        notesRecyclerView.setLayoutManager(linearLayoutManager);

        final Calendar calender = Calendar.getInstance();
        final int hour = calender.get(Calendar.HOUR_OF_DAY);
        final int minute = calender.get(Calendar.MINUTE);
        final int year = calender.get(Calendar.YEAR);
        final int month = calender.get(Calendar.MONTH);
        final int day = calender.get(Calendar.DAY_OF_MONTH);

        timeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHours, int selectedMinute) {
                        Log.i(TAG, "hours: " + selectedHours + " minutes: " + selectedMinute);
                    }

                }, hour, minute, false);

                timePickerDialog.show();
            }
        });


        dateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        Log.i(TAG, "i:" + year + " i1: " + month + " i2: " + day);
                    }
                }, year, month, day);
                datePickerDialog.show();
            }
        });

        addNoteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO fix adding after deletion bug
                notesArrayList.add("");
                Log.i(TAG, notesArrayList.toString());
                addNotesAdapter.notifyDataSetChanged();
            }
        });

        addStartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new PlaceAutocomplete.IntentBuilder()
                        .accessToken(MAPBOX_ACCESS_TOKEN)
                        .placeOptions(PlaceOptions.builder().build(MODE_CARDS))
                        .build(getActivity());
                startActivityForResult(intent, REQUEST_CODE_START_AUTOCOMPLETE);
            }
        });

        addEndButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new PlaceAutocomplete.IntentBuilder()
                        .accessToken(MAPBOX_ACCESS_TOKEN)
                        .placeOptions(PlaceOptions.builder().build(MODE_CARDS))
                        .build(getActivity());
                startActivityForResult(intent, REQUEST_CODE_END_AUTOCOMPLETE);
            }
        });

        return rootView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE_START_AUTOCOMPLETE) {
            CarmenFeature feature = PlaceAutocomplete.getPlace(data);
            //Toast.makeText(getContext(), "start is: " + feature.text(), Toast.LENGTH_LONG).show();
            startPointTextView.setText(feature.text());
        } else if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE_END_AUTOCOMPLETE) {
            CarmenFeature feature = PlaceAutocomplete.getPlace(data);
            Toast.makeText(getContext(), "end is: " + feature.text(), Toast.LENGTH_LONG).show();
            endPointTextView.setText(feature.text());
        }
    }


}
