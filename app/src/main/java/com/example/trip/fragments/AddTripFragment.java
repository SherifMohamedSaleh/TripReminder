package com.example.trip.fragments;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
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

import com.example.trip.R;
import com.example.trip.adapters.AddNotesAdapter;

import java.util.ArrayList;
import java.util.Calendar;

public class AddTripFragment extends Fragment {
    private static final String TAG = "AddTripFragment";
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
                PlacesAutoCompleteFragment fragment = new PlacesAutoCompleteFragment();
                FragmentManager manager = getActivity().getSupportFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                transaction.add(R.id.activity_main, fragment);
                transaction.commit();
            }
        });

        addEndButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PlacesAutoCompleteFragment fragment = new PlacesAutoCompleteFragment();
                FragmentManager manager = getActivity().getSupportFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                transaction.add(R.id.activity_main, fragment);
                transaction.commit();
            }
        });

        return rootView;
    }


}
