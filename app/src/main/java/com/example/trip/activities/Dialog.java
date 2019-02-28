package com.example.trip.activities;


import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Window;

import com.example.trip.models.Trip;
import com.example.trip.utils.FirebaseReferences;
import com.example.trip.utils.NotificationHelper;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;

public class Dialog extends AppCompatActivity implements FirebaseReferences {

    Ringtone ringtone;
    Context context;
    Trip trip;
    String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //  setContentView(R.layout.activity_dialog);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        context = this;
        Bundle bundle = getIntent().getExtras();
        id = bundle.getString("TripID");
        Log.i("", "onCreate: " + id);
        if (firebaseUser != null) {
            tripsRef.child(firebaseUser.getUid()).child(id).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    trip = dataSnapshot.getValue(Trip.class);


                    final AlertDialog.Builder alert = new AlertDialog.Builder(context);
                    alert.setTitle(trip.getTripName() + "");
                    alert.setMessage("checking");
                    alert.setCancelable(false);
                    play();

                    alert.setPositiveButton("Accept", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {

                            Intent intent = new Intent(Dialog.this, HomeNavigationActivity.class);
                            intent.putExtra("Trip", (Serializable) trip);
                            startActivity(intent);
                            ringtone.stop();
                            finish();
                        }
                    });
                    alert.setNegativeButton("Cancle", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                            ringtone.stop();
                            finish();
                        }
                    });
                    alert.setNeutralButton("Later", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            NotificationHelper notificationHelper = new NotificationHelper(context, trip);
                            NotificationCompat.Builder nb = notificationHelper.getChannelNotification();
                            notificationHelper.getManager().notify(1, nb.build());
                            //  sendNotification();

                            dialogInterface.cancel();
                            ringtone.stop();
                            finish();
                        }
                    });

                    alert.create();
                    alert.show();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }
    }


    private void play() {
        Uri sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        ringtone = RingtoneManager.getRingtone(getApplicationContext(), sound);
        ringtone.play();
    }


    public void cancelNotification() {

        String ns = Context.NOTIFICATION_SERVICE;
        NotificationManager nMgr = (NotificationManager) getApplicationContext().getSystemService(ns);
        nMgr.cancel(1);
    }


}