package com.example.trip.utils;


import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

import com.example.trip.R;


public class NotificationHelper extends ContextWrapper {

    public static final String channelID = "channelID";
    public static final String channelName = "Channel Name";

    private NotificationManager mManager;
    Context base;
    int id;

    public NotificationHelper(Context base, int m) {
        super(base);
        this.base = base;
        id = m;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createChannel();
        }
    }

    @TargetApi(Build.VERSION_CODES.O)
    private void createChannel() {
        NotificationChannel channel = new NotificationChannel(channelID, channelName, NotificationManager.IMPORTANCE_HIGH);

        getManager().createNotificationChannel(channel);
    }

    public NotificationManager getManager() {
        if (mManager == null) {
            mManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        }

        return mManager;
    }

    public NotificationCompat.Builder getChannelNotification() {
        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        return new NotificationCompat.Builder(getApplicationContext(), channelID)
                .setContentTitle(id + "")
                .setContentText("Your AlarmManager is working.")
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setAutoCancel(false)
                .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000})
                .setLights(Color.RED, 3000, 3000)
                .setSound(soundUri).setWhen(System.currentTimeMillis())
                .setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_VIBRATE | Notification.DEFAULT_SOUND | Notification.FLAG_SHOW_LIGHTS)
                .setLights(0xff00ff00, 300, 100);
        // .setLatestEventInfo(context, title, message, intent);


//
//        Bundle bundle = new Bundle();
//        bundle.putSerializable("trip", tripData);
//        RoutingFragment fragment = new RoutingFragment();
//        fragment.setArguments(bundle);
//        FragmentTransaction ft = ((AppCompatActivity) context).getSupportFragmentManager().beginTransaction();
//        ft.addToBackStack(null);
//        ft.replace(R.id.fMain, fragment);
//        ft.commit();
//        Intent notificationIntent = new Intent(base, MainActivity.class);
//
//    notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
//            | Intent.FLAG_ACTIVITY_SINGLE_TOP);
//
//    PendingIntent intent = PendingIntent.getActivity(base, 0,
//            notificationIntent, 0);
//
//    notification.setLatestEventInfo(context, title, message, intent);
//    notification.flags |= Notification.FLAG_AUTO_CANCEL;
//    notificationManager.notify(0, notification);


    }
}