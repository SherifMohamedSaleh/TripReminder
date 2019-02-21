package com.example.trip.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.trip.R;
import com.example.trip.models.Note;

import java.util.ArrayList;

/**
 * Created by Toka on 2019-02-15.
 */
public class AddNotesAdapter extends RecyclerView.Adapter<AddNotesAdapter.ViewHolder> {
    private static final String TAG = "AddNotesAdapter";
    private ArrayList<Note> notesArrayList;
    private Context context;

    public AddNotesAdapter(ArrayList<Note> membersArrayList, Context context) {
        this.notesArrayList = membersArrayList;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_note, parent, false);
        Log.i(TAG, "onCreateViewHolder " + notesArrayList.toString());
        EditText editText = view.findViewById(R.id.et_note_name);
        editText.setText("");
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        int i = holder.getLayoutPosition();
        Log.i(TAG, "onBindViewHolder " + notesArrayList.toString());
        if (notesArrayList.get(i).getName().length() > 0)
            holder.noteEditText.setText(notesArrayList.get(i).getName());

    }

    public ArrayList<Note> getNotesArrayList() {
        return notesArrayList;
    }

    @Override
    public int getItemCount() {
        return notesArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageButton deleteButton;
        EditText noteEditText;

        public ViewHolder(View itemView) {
            super(itemView);
            deleteButton = itemView.findViewById(R.id.btn_note_delete);
            noteEditText = itemView.findViewById(R.id.et_note_name);

            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    try {
                        Log.i(TAG, "before remove " + notesArrayList.toString());
                        notesArrayList.remove(position);
                        Log.i(TAG, "after remove " + notesArrayList.toString());
                        notifyItemRemoved(position);
                    } catch (ArrayIndexOutOfBoundsException e) {
                        e.printStackTrace();
                    }
                }
            });

            noteEditText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    notesArrayList.set(getAdapterPosition(), new Note(s.toString(), notesArrayList.get(getAdapterPosition()).isChecked()));
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });

        }
    }
}
