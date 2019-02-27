package com.example.trip.utils;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by Toka on 2019-02-15.
 */
public interface FirebaseReferences {
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference tripsRef = database.getReference("trips");

    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();


}
