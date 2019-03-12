package com.example.trip.adapters;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.trip.R;
import com.example.trip.fragments.RoutingFragment;
import com.example.trip.fragments.TripFragment;
import com.example.trip.models.Trip;
import com.example.trip.utils.AlertReceiver;
import com.example.trip.utils.FirebaseReferences;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ProductViewHolder> implements FirebaseReferences {
    private List<Trip> tripDataList;
    private Context context;
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
    Trip tripData;

    public RecyclerAdapter(Context context, List<Trip> tripDataList) {
        this.tripDataList = tripDataList;
        this.context = context;

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.card, null);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, final int position) {

        final Trip tripData = tripDataList.get(position);
        String str = tripData.getTripName();
        String cap = str.substring(0, 1).toUpperCase() + str.substring(1);
        holder.textOne.setText(cap);
        holder.textTwo.setText(tripData.getDate().getDay() + "/" + (tripData.getDate().getMonth() + 1) + "/" + tripData.getDate().getYear() + ", " + tripData.getTime().getHour() + ":" + tripData.getTime().getMinute());
        if(tripData.getStatus().equals("d"))
        {
            holder.pastImageView.setVisibility(View.VISIBLE);
            holder.upcomingImageView.setVisibility(View.INVISIBLE);
            holder.startTripButton.setVisibility(View.INVISIBLE);
            holder.speedText.setVisibility(View.VISIBLE);
            holder.speedImage.setVisibility(View.VISIBLE);
            if(tripData.getSpeedsCount()==0)
                holder.speedText.setText(" "+Float.toString(tripData.getSpeedSum())+" km/h");
            else
                holder.speedText.setText(" "+Float.toString(tripData.getSpeedSum()/tripData.getSpeedsCount())+" km/h");
        }
        if(tripData.isRoundedTrip()&&tripData.getStatus().equals("h"))
        {
            holder.resumeTripButton.setVisibility(View.VISIBLE);
            holder.startTripButton.setVisibility(View.INVISIBLE);

        }
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                TripFragment tripFragment = new TripFragment();
                tripFragment.setTrip(tripData);
                FragmentTransaction ft = ((AppCompatActivity) context).getSupportFragmentManager().beginTransaction();
                ft.addToBackStack(null);
                ft.replace(R.id.fMain, tripFragment);
                ft.commit();
            }
        });

        holder.startTripButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("trip", tripData);
                RoutingFragment fragment = new RoutingFragment();
                fragment.setArguments(bundle);
                FragmentTransaction ft = ((AppCompatActivity) context).getSupportFragmentManager().beginTransaction();
                ft.addToBackStack(null);
                ft.replace(R.id.fMain, fragment);
                ft.commit();
            }
        });
        holder.resumeTripButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("trip", tripData);
                RoutingFragment fragment = new RoutingFragment();
                fragment.setArguments(bundle);
                FragmentTransaction ft =((AppCompatActivity) context).getSupportFragmentManager().beginTransaction();
                ft.addToBackStack(null);
                ft.replace(R.id.fMain, fragment);
                ft.commit();
            }
        });

        holder.deleteTrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(context)
                        .setTitle("Delete trip")
                        .setMessage("Are you sure you want to delete this trip?")
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int whichButton) {
                                AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                                Intent intent = new Intent(context, AlertReceiver.class);
                                PendingIntent pendingIntent = PendingIntent.getBroadcast(context, tripData.getTripRequestId(), intent, PendingIntent.FLAG_CANCEL_CURRENT);
                                pendingIntent.getCreatorUid();
                                alarmManager.cancel(pendingIntent);
                                tripDataList.remove(position);
                                tripsRef.child(firebaseUser.getUid()).child(tripData.getId()).removeValue();
                                notifyItemRemoved(position);

                            }
                        })
                        .setNegativeButton(android.R.string.no, null).show();

            }
        });

    }

    private void cancelAlarm() {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlertReceiver.class);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, tripData.getTripRequestId(), intent, PendingIntent.FLAG_CANCEL_CURRENT);

        pendingIntent.getCreatorUid();
        alarmManager.cancel(pendingIntent);

    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public int getItemCount() {
        return tripDataList.size();
    }

    class ProductViewHolder extends RecyclerView.ViewHolder {

        TextView textOne, textTwo,speedText;
        CardView cardView;
        ImageButton startTripButton,resumeTripButton;
        ImageButton deleteTrip;
        ImageView speedImage;
        ImageView upcomingImageView,pastImageView;

        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        public ProductViewHolder(View itemView) {
            super(itemView);

            textOne = itemView.findViewById(R.id.textOne);
            textTwo = itemView.findViewById(R.id.textTwo);
            speedImage=itemView.findViewById(R.id.speedImageView);
            speedText = itemView.findViewById(R.id.speedView);
            cardView = (CardView) itemView.findViewById(R.id.cardview_id);
            upcomingImageView=itemView.findViewById(R.id.upcomingImageView);
            startTripButton = itemView.findViewById(R.id.btn_start_trip);
            deleteTrip = itemView.findViewById(R.id.deleteTripBtn);
            pastImageView=itemView.findViewById(R.id.pastImageView);
            resumeTripButton=itemView.findViewById(R.id.resumeRoundBtn);
            upcomingImageView.setElevation(10);
            pastImageView.setElevation(10);
            cardView.setElevation(5);
            deleteTrip.setElevation(10);
        }
    }
}