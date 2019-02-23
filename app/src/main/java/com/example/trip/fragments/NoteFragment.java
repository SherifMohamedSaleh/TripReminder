package com.example.trip.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import com.example.trip.R;
import com.example.trip.adapters.AddNotesAdapter;
import com.example.trip.models.Note;
import com.example.trip.models.Trip;
import com.example.trip.utils.FirebaseReferences;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class NoteFragment extends Fragment implements FirebaseReferences {

    ImageButton addNote;
    Button saveNotes;
    Trip trip;
    ArrayList<Note> notesArrayList;
    AddNotesAdapter addNotesAdapter;
    RecyclerView notesRecyclerView;
    LinearLayoutManager linearLayoutManager;

    public NoteFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_note, container, false);
        notesArrayList = trip.getNotes();
        notesRecyclerView = view.findViewById(R.id.rv_notes);
        addNotesAdapter = new AddNotesAdapter(notesArrayList, getContext());
        addNote=view.findViewById(R.id.addNoteBtn);
        saveNotes=view.findViewById(R.id.saveTrip);
        linearLayoutManager = new LinearLayoutManager(getContext());
        notesRecyclerView.setAdapter(addNotesAdapter);
        notesRecyclerView.setLayoutManager(linearLayoutManager);

        addNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notesArrayList.add(new Note("", false));
                addNotesAdapter.notifyDataSetChanged();
            }
        });
        saveNotes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                tripsRef.child(firebaseUser.getUid()).child(trip.getId()).setValue(trip);
                TripFragment tripFragment=new TripFragment();
                FragmentManager fragmentManager=getFragmentManager();
                FragmentTransaction ft= fragmentManager.beginTransaction();
                trip.setNotes(notesArrayList);
                tripFragment.setTrip(trip);
                ft.replace(R.id.fMain,tripFragment);
                ft.commit();

            }
        });
        return view;
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        FragmentManager fragmentManager=getFragmentManager();
        fragmentManager.popBackStack();
    }
    public void setTrip(Trip trip)
    {
        this.trip=trip;
    }

}
