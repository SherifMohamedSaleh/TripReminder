package com.example.trip.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.trip.R;

public class TempGoogleNavigationAcitivy extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_temp_google_navigation_acitivy);

        /*Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                Uri.parse("http://maps.google.com/maps?saddr=31.2,29.91667 ("+"Alexxx"+")&daddr=30.05611,31.23944 ("+"Cairooo"+")&travelmode=driving"));
        startActivity(intent);*/

        /*Uri gmmIntentUri = Uri.parse("google.navigation:q=31.2,29.91667 ; 30.05611,31.23944");
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        startActivity(mapIntent);*/


        Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                Uri.parse("https://www.google.com/maps/dir/?api=1&origin=Paris,France&destination=Cherbourg,France&travelmode=driving&waypoints=Versailles,France%7CChartres,France%7CLe+Mans,France%7CCaen,France"));
        startActivityForResult(intent, 123);
    }


}
