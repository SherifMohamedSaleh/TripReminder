package com.example.trip.utils;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.trip.activities.Dialog;


public class AlertReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        int id = intent.getIntExtra("id", 0);
        Log.d("TripFragment", "onReceive:     :" + id);
        Intent trIntent = new Intent("android.intent.action.MAIN");
        trIntent.setClass(context, Dialog.class);
        //  trIntent.setFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
        trIntent.putExtra("id", id);
        context.startActivity(trIntent);
        Log.d("AlertReceiver", "onReceive: ");
    }
}