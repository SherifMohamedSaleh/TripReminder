package com.example.trip.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.trip.R;
import com.example.trip.fragments.TripFragment;
import com.example.trip.models.Trip;

import java.util.List;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ProductViewHolder> {
    private List<Trip> tripDataList;
    private Context context;

    public RecyclerAdapter(Context context, List<Trip> tripDataList) {
        this.tripDataList = tripDataList;
        this.context = context;
    }

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
        holder.textOne.setText(tripData.getTripName());
        holder.textTwo.setText("sat");
        holder.textThree.setText("10:30");
        // holder.textFour.setText(tripData.getDate()+"");
        //   holder.textFive.setText(tripData.getTripType()+"");
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                TripFragment tripFragment = new TripFragment();
                tripFragment.setTrip(tripData);
                FragmentTransaction ft = ((AppCompatActivity) context).getSupportFragmentManager().beginTransaction();
                ft.addToBackStack(null);
                ft.replace(R.id.fMain, tripFragment);
                ft.commit();
                //Toast.makeText(context, " hello to another screen  : item  " + position + "  selected", Toast.LENGTH_SHORT).show();
            }
        });

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

        TextView textOne, textTwo, textThree, textFour, textFive;
        CardView cardView;


        public ProductViewHolder(View itemView) {
            super(itemView);

            textOne = itemView.findViewById(R.id.textOne);
            textTwo = itemView.findViewById(R.id.textTwo);
            textThree = itemView.findViewById(R.id.textThree);
            //     textFour = itemView.findViewById(R.id.textFour);
            //   textFive = itemView.findViewById(R.id.textFive);
            cardView = (CardView) itemView.findViewById(R.id.cardview_id);
        }
    }
}
