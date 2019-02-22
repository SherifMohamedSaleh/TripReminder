package com.example.trip.utils;

import com.example.trip.models.Note;
import com.example.trip.models.Trip;

import java.util.ArrayList;

public interface Communicator {
    public void tripResponse(Trip trip);
    public void notesResponse(ArrayList<Note> notes);
}
