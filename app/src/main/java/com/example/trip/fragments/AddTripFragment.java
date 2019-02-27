package com.example.trip.fragments;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
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
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.trip.R;
import com.example.trip.adapters.AddNotesAdapter;
import com.example.trip.models.Note;
import com.example.trip.models.Trip;
import com.example.trip.models.TripDate;
import com.example.trip.models.TripLocation;
import com.example.trip.models.TripTime;
import com.example.trip.utils.AlertReceiver;
import com.example.trip.utils.FirebaseReferences;
import com.mapbox.api.geocoding.v5.models.CarmenFeature;
import com.mapbox.mapboxsdk.plugins.places.autocomplete.PlaceAutocomplete;
import com.mapbox.mapboxsdk.plugins.places.autocomplete.model.PlaceOptions;

import java.util.ArrayList;
import java.util.Calendar;

import static com.mapbox.mapboxsdk.plugins.places.autocomplete.model.PlaceOptions.MODE_CARDS;

public class AddTripFragment extends Fragment implements FirebaseReferences /*,TimePickerDialog.OnTimeSetListener  ,DatePickerDialog.OnDateSetListener */ {
    private static final String TAG = "AddTripFragment";
    private static final String MAPBOX_ACCESS_TOKEN = "sk.eyJ1IjoidG9rYWFsaWFtaW4iLCJhIjoiY2pzODBzcjlrMTJ4azN5bnV6a3E2cTJiaSJ9.jWdMw48rKqQ9t-cd8J0KBA";
    private static final int REQUEST_CODE_START_AUTOCOMPLETE = 1;
    private static final int REQUEST_CODE_END_AUTOCOMPLETE = 2;

    TextInputEditText tripNameEditText;
    ImageButton timeButton, dateButton, addNoteButton, addStartButton, addEndButton;
    RecyclerView notesRecyclerView;
    TextView startPointTextView, endPointTextView;
    Switch isRoundedSwitch;

    Button doneButton;

    ArrayList<Note> notesArrayList;
    AddNotesAdapter addNotesAdapter;
    LinearLayoutManager linearLayoutManager;
    Calendar calender;

    Trip trip;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
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
        isRoundedSwitch = rootView.findViewById(R.id.switch_rounded_trip);
        doneButton = rootView.findViewById(R.id.btn_done);

        notesArrayList = new ArrayList<>();
        addNotesAdapter = new AddNotesAdapter(notesArrayList, getContext());

        linearLayoutManager = new LinearLayoutManager(getContext());
        notesRecyclerView.setAdapter(addNotesAdapter);
        notesRecyclerView.setLayoutManager(linearLayoutManager);

        trip = new Trip();


        calender = Calendar.getInstance();
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
                        trip.setTime(new TripTime(selectedHours, selectedMinute));
                        calender.set(Calendar.HOUR_OF_DAY, selectedHours);
                        calender.set(Calendar.MINUTE, selectedMinute);
                        calender.set(Calendar.SECOND, 0);

                        Log.i(TAG, " calender1 hours: " + selectedHours + " minutes: " + selectedMinute);
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
                        trip.setDate(new TripDate(day, month, year));
                        calender.set(Calendar.YEAR, year);
                        calender.set(Calendar.MONTH, month);
                        calender.set(Calendar.DAY_OF_MONTH, day);

                    }
                }, year, month, day);
                datePickerDialog.show();
            }
        });

        addNoteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO fix adding after deletion bug
                notesArrayList.add(new Note("", false));
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

        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (addNotesAdapter.getNotesArrayList() != null)
                    trip.setNotes(addNotesAdapter.getNotesArrayList());

                trip.setTripName(tripNameEditText.getText().toString());

                trip.setRoundedTrip(isRoundedSwitch.isChecked());

                int m = startAlarm(calender);

                trip.setTripRequestId(m);
                String key = tripsRef.child(firebaseUser.getUid()).push().getKey();
                trip.setId(key);
                tripsRef.child(firebaseUser.getUid()).child(key).setValue(trip);


            }
        });

        return rootView;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private int startAlarm(Calendar calendar) {

        Log.i(TAG, "StartAlarm  hours: " + calendar.get(calendar.HOUR) + " minutes: " + calendar.get(calendar.MINUTE));
        Log.i(TAG, "StartAlarm  Year: " + calendar.get(calendar.YEAR) + " Month: " + calendar.get(calendar.MONTH) + " Day" + calendar.get(calendar.DAY_OF_MONTH));


        AlarmManager alarmManager = (AlarmManager) getContext().getSystemService(Context.ALARM_SERVICE);
        int x = (int) System.currentTimeMillis();// Calendar.getInstance().getTimeInMillis();
        Intent intent = new Intent(getContext(), AlertReceiver.class);
        // intent.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
        intent.putExtra("id", x);
        Log.i("AlertReceiver", "id" + x);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getContext(), x, intent, 0);

        if (calendar.before(Calendar.getInstance())) {
            calendar.add(Calendar.DATE, 1);
        }


        //1551027300469

        alarmManager.setExact(alarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
        Log.i(TAG, "startAlarm:      Alerm Added " + calendar.getTimeInMillis());
        return x;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE_START_AUTOCOMPLETE) {
            CarmenFeature feature = PlaceAutocomplete.getPlace(data);
            startPointTextView.setText(feature.text());
            trip.setStartPoint(new TripLocation(feature.center().latitude(), feature.center().longitude(), feature.text()));
        } else if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE_END_AUTOCOMPLETE) {
            CarmenFeature feature = PlaceAutocomplete.getPlace(data);
            endPointTextView.setText(feature.text());
            trip.setEndPoint(new TripLocation(feature.center().latitude(), feature.center().longitude(), feature.text()));

        }
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
