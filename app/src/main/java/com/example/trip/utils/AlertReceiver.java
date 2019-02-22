package com.example.trip.utils;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.trip.activities.Dialog;


public class AlertReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String id = intent.getStringExtra("id");
        Log.d("AlertReceiver", "onReceive:     :" + id);
        Intent trIntent = new Intent("android.intent.action.MAIN");
        trIntent.setClass(context, Dialog.class);
        trIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        trIntent.putExtra("id", id);
        context.startActivity(trIntent);
        Log.d("AlertReceiver", "onReceive: ");
    }
}