package com.example.trip.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.trip.R;
import com.example.trip.models.Trip;

import java.util.List;

public class RecyclerAdapter extends RecyclerView.Adapter<ViewHolder> {
    private List<Trip> tripList;

    public RecyclerAdapter(List<Trip> tripList) {
        this.tripList = tripList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_view_item, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        Trip trip = tripList.get(i);
        viewHolder.textOne.setText(trip.getTripName());
       /* viewHolder.textTwo.setText(trip.getStartAddress());
        viewHolder.textThree.setText(trip.getEndAddress());
        viewHolder.textFour.setText(trip.getDate().toString());
        viewHolder.textFive.setText(trip.getIsRoundedTrip());*/
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
