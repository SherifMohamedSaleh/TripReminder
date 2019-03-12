package com.example.trip.fragments;


import android.app.Activity;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.trip.R;
import com.example.trip.adapters.AddNotesAdapter;
import com.example.trip.models.Note;
import com.example.trip.models.Trip;
import com.example.trip.models.TripDate;
import com.example.trip.models.TripLocation;
import com.example.trip.models.TripTime;
import com.example.trip.utils.AlertReceiver;
import com.example.trip.utils.FirebaseReferences;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.mapbox.api.geocoding.v5.models.CarmenFeature;
import com.mapbox.mapboxsdk.plugins.places.autocomplete.PlaceAutocomplete;
import com.mapbox.mapboxsdk.plugins.places.autocomplete.model.PlaceOptions;

import java.util.ArrayList;
import java.util.Calendar;

import static com.mapbox.mapboxsdk.plugins.places.autocomplete.model.PlaceOptions.MODE_CARDS;

public class TripFragment extends Fragment implements FirebaseReferences {

    private static final String TAG = "TripFragment";
    Calendar calender = Calendar.getInstance();
    Calendar calender1 = Calendar.getInstance();
    int hour = calender.get(Calendar.HOUR_OF_DAY);
    int minute = calender.get(Calendar.MINUTE);
    int year = calender.get(Calendar.YEAR);
    int month = calender.get(Calendar.MONTH);
    int day = calender.get(Calendar.DAY_OF_MONTH);
    Boolean changeAlarm = false;
    private static final String MAPBOX_ACCESS_TOKEN = "sk.eyJ1IjoidG9rYWFsaWFtaW4iLCJhIjoiY2pzODBzcjlrMTJ4azN5bnV6a3E2cTJiaSJ9.jWdMw48rKqQ9t-cd8J0KBA";
    private static final int REQUEST_CODE_START_AUTOCOMPLETE = 1;
    private static final int REQUEST_CODE_END_AUTOCOMPLETE = 2;
    ArrayList<Note> notesArrayList;
    AddNotesAdapter addNotesAdapter;
    RecyclerView notesRecyclerView;
    LinearLayoutManager linearLayoutManager;
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
    EditText tripName, tripDate, tripTime, tripSource, tripDest;
    FloatingActionButton saveTrip;
    Button notesBtn, roundedBtn;
    ImageButton addNote;
    TextView tripStatus;
    CheckBox doneCheckBox;
    Trip trip;

    public TripFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_trip, container, false);
        tripName = view.findViewById(R.id.tripNameVal);
        tripStatus = view.findViewById(R.id.tripStatusVal);
        tripDate = view.findViewById(R.id.tripDateVal);
        tripTime = view.findViewById(R.id.tripTimeVal);
        tripSource = view.findViewById(R.id.tripSourceVal);
        tripDest = view.findViewById(R.id.tripDestVal);
        doneCheckBox = view.findViewById(R.id.doneCheckBox);
        roundedBtn = view.findViewById(R.id.resumeRoundBtn);
        saveTrip = view.findViewById(R.id.saveTripBtn);
        notesArrayList = trip.getNotes();
        notesRecyclerView = view.findViewById(R.id.rv_notes);
        addNotesAdapter = new AddNotesAdapter(notesArrayList, getContext());
        addNote = view.findViewById(R.id.addNoteBtn);
        //   saveNotes = view.findViewById(R.id.saveTrip);
        linearLayoutManager = new LinearLayoutManager(getContext());
        notesRecyclerView.setAdapter(addNotesAdapter);
        notesRecyclerView.setLayoutManager(linearLayoutManager);
        doneCheckBox.setClickable(false);
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
        } else if (trip.getStatus().equals("h")) {
            tripStatus.setText("Half finished");
            doneCheckBox.setChecked(false);
        }


        tripDate.setText(Integer.toString(trip.getDate().getDay()) + "/" + Integer.toString(trip.getDate().getMonth()) + "/" + Integer.toString(trip.getDate().getYear()));
        tripTime.setText(Integer.toString(trip.getTime().getHour()) + ":" + Integer.toString(trip.getTime().getMinute()));
        tripSource.setText("• " + trip.getStartPoint().getAddress());
        tripDest.setText("• " + trip.getEndPoint().getAddress());


            tripTime.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker timePicker, int selectedHours, int selectedMinute) {
                            trip.setTime(new TripTime(selectedHours, selectedMinute));
                            tripTime.setText(Integer.toString(trip.getTime().getHour())+":"+Integer.toString(trip.getTime().getMinute()));
                            calender1.set(Calendar.HOUR_OF_DAY, selectedHours);
                            calender1.set(Calendar.MINUTE, selectedMinute);
                            calender1.set(Calendar.SECOND, 0);
                            changeAlarm = true;
                        }
                    }, hour, minute, false);

                    timePickerDialog.show();
                    if(hasFocus) {
                        tripTime.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                                    @Override
                                    public void onTimeSet(TimePicker timePicker, int selectedHours, int selectedMinute) {
                                        trip.setTime(new TripTime(selectedHours, selectedMinute));
                                        tripTime.setText(Integer.toString(trip.getTime().getHour())+":"+Integer.toString(trip.getTime().getMinute()));
                                        calender1.set(Calendar.HOUR_OF_DAY, selectedHours);
                                        calender1.set(Calendar.MINUTE, selectedMinute);
                                        calender1.set(Calendar.SECOND, 0);
                                        changeAlarm = true;
                                    }
                                }, hour, minute, false);
                            }
                        });
                    }
                     }
            });
            tripDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                            trip.setDate(new TripDate(day, month, year));
                            tripDate.setText(Integer.toString(trip.getDate().getDay()) + "/" + Integer.toString(trip.getDate().getMonth()) + "/" + Integer.toString(trip.getDate().getYear()));
                            calender.set(Calendar.YEAR, year);
                            calender.set(Calendar.MONTH, month);
                            calender.set(Calendar.DAY_OF_MONTH, day);
                        }
                    }, year, month, day);
                    datePickerDialog.show();
                    if(hasFocus) {
                        tripDate.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                                    @Override
                                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                                        trip.setDate(new TripDate(day, month, year));
                                        tripDate.setText(Integer.toString(trip.getDate().getDay()) + "/" + Integer.toString(trip.getDate().getMonth()) + "/" + Integer.toString(trip.getDate().getYear()));
                                        calender.set(Calendar.YEAR, year);
                                        calender.set(Calendar.MONTH, month);
                                        calender.set(Calendar.DAY_OF_MONTH, day);
                                    }
                                }, year, month, day);
                                datePickerDialog.show();
                            }
                        });
                    }

                }
            });
            tripSource.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if(hasFocus) {
                        Intent intent = new PlaceAutocomplete.IntentBuilder()
                                .accessToken(MAPBOX_ACCESS_TOKEN)
                                .placeOptions(PlaceOptions.builder().build(MODE_CARDS))
                                .build(getActivity());
                        startActivityForResult(intent, REQUEST_CODE_START_AUTOCOMPLETE);
                        tripSource.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new PlaceAutocomplete.IntentBuilder()
                                        .accessToken(MAPBOX_ACCESS_TOKEN)
                                        .placeOptions(PlaceOptions.builder().build(MODE_CARDS))
                                        .build(getActivity());
                                startActivityForResult(intent, REQUEST_CODE_START_AUTOCOMPLETE);
                            }
                        });


                    }}

            });
            tripDest.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    Intent intent = new PlaceAutocomplete.IntentBuilder()
                            .accessToken(MAPBOX_ACCESS_TOKEN)
                            .placeOptions(PlaceOptions.builder().build(MODE_CARDS))
                            .build(getActivity());
                    startActivityForResult(intent, REQUEST_CODE_END_AUTOCOMPLETE);
                    if(hasFocus) {
                        tripDest.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new PlaceAutocomplete.IntentBuilder()
                                        .accessToken(MAPBOX_ACCESS_TOKEN)
                                        .placeOptions(PlaceOptions.builder().build(MODE_CARDS))
                                        .build(getActivity());
                                startActivityForResult(intent, REQUEST_CODE_END_AUTOCOMPLETE);
                            }
                        });

                    } }
            });
        addNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notesArrayList.add(new Note("", false));
                addNotesAdapter.notifyDataSetChanged();
            }
        });
        saveTrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isEmpty(tripName)) {
                    if (!isEmpty(tripDate)) {
                        if (!isEmpty(tripTime)) {
                            if (!isEmpty(tripSource)) {
                                if (!isEmpty(tripDest)) {
                                    if (addNotesAdapter.getNotesArrayList() != null)
                                       trip.setNotes(addNotesAdapter.getNotesArrayList());
                                    if(changeAlarm == true) {
                                        startAlarm(calender1);
                                    }
                                    trip.setTripName(tripName.getText().toString());
                                    tripsRef.child(firebaseUser.getUid()).child(trip.getId()).setValue(trip).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Toast.makeText(getActivity(), "Trip saved", Toast.LENGTH_SHORT).show();
                                            FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                                            ft.replace(R.id.fMain, new UpComingFragment());
                                            ft.addToBackStack(null);
                                            ft.commit();
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(getActivity(), "Failed to save trip", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                } else {
                                    //if end point is not set
                                    Toast.makeText(getActivity(), "End point cannot be empty", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                //if start point is not set
                                Toast.makeText(getActivity(), "Start point cannot be empty", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            //if time is not set
                            Toast.makeText(getActivity(), "Time cannot be empty", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        //if date is not set
                        Toast.makeText(getActivity(), "Date cannot be empty", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // if name is empty
                    Toast.makeText(getActivity(), "Name cannot be empty", Toast.LENGTH_SHORT).show();
                }

            }
        });

        return view;
    }

    private void startAlarm(Calendar calendar) {

        AlarmManager alarmManager = (AlarmManager) getContext().getSystemService(getContext().ALARM_SERVICE);
        int x = trip.getTripRequestId();//(int)calendar.getTimeInMillis();
        Log.i(TAG, "startAlarm: " + x);
        Intent intent = new Intent(getContext(), AlertReceiver.class);
        //   intent.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
        intent.putExtra("TripID", trip.getId());
        Log.i("AlertReceiver", "TripID" + x);
        //  trip.setTripRequestId(id);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getContext(), x, intent, PendingIntent.FLAG_UPDATE_CURRENT);


        Log.i(TAG, "StartAlarm  hours: " + calendar.get(calendar.HOUR) + " minutes: " + calendar.get(calendar.MINUTE));
        Log.i(TAG, "StartAlarm  Year: " + calendar.get(calendar.YEAR) + " Month: " + calendar.get(calendar.MONTH) + " Day" + calendar.get(calendar.DAY_OF_MONTH));


        pendingIntent.getCreatorUid();
        alarmManager.setExact(alarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
        Log.i("AlertReceiver", "startAlarm:     Alerm updated " + calendar.getTimeInMillis());

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE_START_AUTOCOMPLETE) {
            CarmenFeature feature = PlaceAutocomplete.getPlace(data);
            tripSource.setText("• " + feature.text());
            trip.setStartPoint(new TripLocation(feature.center().latitude(), feature.center().longitude(), feature.text()));
        } else if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE_END_AUTOCOMPLETE) {
            CarmenFeature feature = PlaceAutocomplete.getPlace(data);
            tripDest.setText("• " + feature.text());
            trip.setEndPoint(new TripLocation(feature.center().latitude(), feature.center().longitude(), feature.text()));
        }
    }
    public void setTrip(Trip trip) {
        this.trip = trip;
    }
    private boolean isEmpty(EditText editText) {
        return editText.getText().toString().length() <= 0 || editText.getText() == null;
    }
}