package com.example.trip.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.trip.R;
import com.example.trip.fragments.PastTripsFragment;
import com.example.trip.fragments.RoutingFragment;
import com.example.trip.fragments.SettingsFragment;
import com.example.trip.fragments.UpComingFragment;
import com.example.trip.models.Trip;
import com.example.trip.utils.FirebaseReferences;
import com.google.firebase.auth.FirebaseAuth;


public class HomeNavigationActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, FirebaseReferences {

    private static final String TAG = "HomeNavigationActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_navigation);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.addToBackStack(null);
        if (getIntent().getExtras() != null) {
            if (getIntent().getExtras().getSerializable("Trip") != null) {
                Trip trip = (Trip) getIntent().getExtras().getSerializable("Trip");
                Log.i(TAG, "onCreate: trip name" + trip.getTripName());
                Log.i(TAG, "onCreate: trip ID" + trip.getId());
                Log.i(TAG, "onCreate:  End Point" + trip.getEndPoint().getAddress());
                Log.i(TAG, "onCreate: Start Point" + trip.getStartPoint().getAddress());
                Bundle bundle = new Bundle();
                bundle.putSerializable("trip", trip);
                RoutingFragment fragment = new RoutingFragment();
                fragment.setArguments(bundle);
                //    FragmentTransaction ft = ((AppCompatActivity) context).getSupportFragmentManager().beginTransaction();
                ft.addToBackStack(null);
                ft.replace(R.id.fMain, fragment);
                ft.commit();

            }
        } else {
            ft.replace(R.id.fMain, new UpComingFragment());
            ft.commit();
        }

        navigationView.setCheckedItem(R.id.nav_home);

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home_navigation, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.nav_home) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.fMain, new UpComingFragment());
            ft.addToBackStack(null);
            ft.commit();
        } else if (id == R.id.nav_trips) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.addToBackStack(null);
            ft.replace(R.id.fMain, new PastTripsFragment());
            ft.commit();
        } else if (id == R.id.nav_sync) {

        } else if (id == R.id.nav_settings) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.addToBackStack(null);
            ft.replace(R.id.fMain, new SettingsFragment());
            ft.commit();
        } else if (id == R.id.nav_logout) {

            FirebaseAuth.getInstance().signOut();
            if (FirebaseAuth.getInstance().getCurrentUser() == null) {
                Intent intent = new Intent(HomeNavigationActivity.this, MainActivity.class);
                startActivity(intent);
            }
            // [END auth_sign_out]

   /*         AlertDialog.Builder builder;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                builder = new AlertDialog.Builder(getApplicationContext(), android.R.style.Theme_Material_Dialog_Alert);
            } else {
                builder = new AlertDialog.Builder(getApplicationContext());
            }
            builder.setTitle("Logout").setMessage("Are you sure you want to logout?").setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    //logout code
                }
            });
            AlertDialog alert=builder.create();
            alert.show();
            */
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
