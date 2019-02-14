package com.example.trip.activities;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.TimePicker;

import com.example.trip.R;
import com.example.trip.adapters.AddNotesAdapter;

import java.util.ArrayList;
import java.util.Calendar;

public class AddTripActivity extends AppCompatActivity {
    private static final String TAG = "AddTripActivity";
    TextInputEditText tripNameEditText;
    ImageButton timeButton, dateButton, addNoteButton;
    RecyclerView notesRecyclerView;

    ArrayList<String> notesArrayList;
    AddNotesAdapter addNotesAdapter;
    LinearLayoutManager linearLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_trip);

        tripNameEditText = findViewById(R.id.et_trip_name);
        timeButton = findViewById(R.id.btn_trip_time);
        dateButton = findViewById(R.id.btn_trip_date);
        addNoteButton = findViewById(R.id.btn_add_note);
        notesRecyclerView = findViewById(R.id.rv_notes);

        notesArrayList = new ArrayList<>();
        addNotesAdapter = new AddNotesAdapter(notesArrayList, this);

        linearLayoutManager = new LinearLayoutManager(this);
        notesRecyclerView.setAdapter(addNotesAdapter);
        notesRecyclerView.setLayoutManager(linearLayoutManager);

        final Calendar calender = Calendar.getInstance();
        final int hour = calender.get(Calendar.HOUR_OF_DAY);
        final int minute = calender.get(Calendar.MINUTE);

        timeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(AddTripActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHours, int selectedMinute) {
                        Log.i(TAG, "hours: " + selectedHours + " minutes: " + selectedMinute);
                    }

                }, hour, minute, false);

                timePickerDialog.show();
            }
        });

        final int year = calender.get(Calendar.YEAR);
        final int month = calender.get(Calendar.MONTH);
        final int day = calender.get(Calendar.DAY_OF_MONTH);

        dateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(AddTripActivity.this, new DatePickerDialog.OnDateSetListener() {

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


    }
}
