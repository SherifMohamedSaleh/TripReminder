package com.example.trip.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.trip.R;
import com.example.trip.models.TripData;

import java.util.List;

public class RecyclerAdapter extends RecyclerView.Adapter<ViewHolder> {
    private List<TripData> tripDataList;

    public RecyclerAdapter(List<TripData> tripDataList) {
        this.tripDataList = tripDataList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_view_item, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        TripData tripData = tripDataList.get(i);
        viewHolder.textOne.setText(tripData.getTripName());
        viewHolder.textTwo.setText(tripData.getStartPoint());
        viewHolder.textThree.setText(tripData.getEndPoint());
        viewHolder.textFour.setText(tripData.getDate().toString());
        viewHolder.textFive.setText(tripData.getTripType());
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
