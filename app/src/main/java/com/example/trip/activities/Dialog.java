package com.example.trip.activities;


import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Window;

import com.example.trip.utils.NotificationHelper;

public class Dialog extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //  setContentView(R.layout.activity_dialog);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        final Context ctx = this;
        Bundle bundle = getIntent().getExtras();
        int id = bundle.getInt("id", 0);
        Log.d("AlertReceiver", "onReceive:     :" + id);
        Log.d("dialog", "onCreate:     :" + id);

        final AlertDialog.Builder alert = new AlertDialog.Builder(ctx);
        alert.setTitle(id + "");
        alert.setMessage("checking");
        alert.setCancelable(false);

        alert.setPositiveButton("Accept", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

                finish();
            }
        });
        alert.setNegativeButton("Cancle", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                NotificationHelper notificationHelper = new NotificationHelper(ctx);
                NotificationCompat.Builder nb = notificationHelper.getChannelNotification();
                notificationHelper.getManager().notify(1, nb.build());
                dialogInterface.cancel();
                finish();
            }
        });

        alert.create();
        alert.show();


    }
}