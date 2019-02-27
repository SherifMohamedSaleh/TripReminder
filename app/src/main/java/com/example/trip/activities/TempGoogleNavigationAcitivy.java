package com.example.trip.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.trip.R;

public class TempGoogleNavigationAcitivy extends AppCompatActivity {
    Button button;

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
        button = findViewById(R.id.btn_nav1);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                        Uri.parse("https://www.google.com/maps/dir/?api=1&origin=31.2,29.91667&destination=30.05611,31.23944&travelmode=driving"));

                /*Uri uri = Uri.parse("geo:" + 31.2 + "," + 29.91667 + "?q=" + Uri.encode(30.05611 + "," + 31.23944 + "(" +"Cairo"+ ")"));
                االليبول اشتغل بس مش بينافجيت من اسكندريه
                 */
                //Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("geo:31.2,29.91667?q=30.05611,31.23944(Label Name)"));


                startActivityForResult(intent, 123);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 123) {
            if (resultCode == RESULT_OK) {
                Toast.makeText(this, "ON reult", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
