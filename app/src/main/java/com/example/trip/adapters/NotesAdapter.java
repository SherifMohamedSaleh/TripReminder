package com.example.trip.adapters;

import android.content.Context;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.trip.R;
import com.example.trip.models.Note;

import java.util.ArrayList;

/**
 * Created by Toka on 2019-02-23.
 */
public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.ViewHolder> {
    private static final String TAG = "NotesAdapter";
    private ArrayList<Note> notesArrayList;
    private Context context;

    public NotesAdapter(ArrayList<Note> notesArrayList, Context context) {
        this.notesArrayList = notesArrayList;
        this.context = context;
    }

    @Override
    public NotesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_route_note, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final NotesAdapter.ViewHolder holder, int position) {
        int i = holder.getLayoutPosition();
        if (notesArrayList.get(i).getName().length() > 0)
            holder.noteTextView.setText(notesArrayList.get(i).getName());

        holder.noteTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(TAG, "note clicked");
                int flags = holder.noteTextView.getPaintFlags();
                if (flags == Paint.STRIKE_THRU_TEXT_FLAG) {
                    holder.noteTextView.setPaintFlags(0);
                } else if (flags == 0) {
                    holder.noteTextView.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                }
            }
        });


    }


    @Override
    public int getItemCount() {
        return notesArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView noteTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            noteTextView = itemView.findViewById(R.id.tv_route_note);
        }
    }
}
