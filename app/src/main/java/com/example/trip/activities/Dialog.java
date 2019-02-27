package com.example.trip.activities;


import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.media.AudioAttributes;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Window;

import com.example.trip.R;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //  setContentView(R.layout.activity_dialog);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        context = this;
        Bundle bundle = getIntent().getExtras();
        final String id = bundle.getString("TripID");
        Log.i("", "onCreate: " + id);
        if (firebaseUser != null) {
            tripsRef.child(firebaseUser.getUid()).child(id).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    final Trip trip = dataSnapshot.getValue(Trip.class);


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
                            NotificationHelper notificationHelper = new NotificationHelper(context, trip.getTripRequestId());
                            NotificationCompat.Builder nb = notificationHelper.getChannelNotification();
                            notificationHelper.getManager().notify(1, nb.build());

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

    private void showNotification() {


        String id = "main_channel";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            CharSequence name = "Channel name";
            String description = "Channel Description";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            AudioAttributes audioAttributes = new AudioAttributes.Builder().setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .setUsage(AudioAttributes.USAGE_NOTIFICATION_RINGTONE).build();
            NotificationChannel notificationChannel = new NotificationChannel(id, name, importance);
            notificationChannel.setDescription(description);
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.WHITE);
            notificationChannel.enableVibration(false);
            notificationChannel.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION), audioAttributes);

            if (notificationManager != null) {
                notificationManager.createNotificationChannel(notificationChannel);
            }
            //head to the second activity
            Intent mainIntent = new Intent(this, MainActivity.class);
            //context.startActivity ( mainIntent );
            PendingIntent contentIntent = PendingIntent.getActivity(this, 0, mainIntent, 0);

            //notification

            Notification.Builder notificationBuilder = new Notification.Builder(this, id);
            notificationBuilder.setSmallIcon(R.mipmap.ic_launcher);
            notificationBuilder.setContentTitle("Reminder");

            notificationBuilder.setLights(Color.WHITE, 500, 5000);
            notificationBuilder.setColor(Color.RED);
            notificationBuilder.setDefaults(Notification.DEFAULT_SOUND);
            notificationBuilder.setContentIntent(contentIntent);
            notificationBuilder.getNotification().flags |= Notification.FLAG_AUTO_CANCEL;
            notificationManager.notify(0, notificationBuilder.build());

        }
    }
}