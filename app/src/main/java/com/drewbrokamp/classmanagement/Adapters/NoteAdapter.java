package com.drewbrokamp.classmanagement.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.drewbrokamp.classmanagement.Model.Note;
import com.drewbrokamp.classmanagement.R;
import com.drewbrokamp.classmanagement.Util.NoteRecyclerViewInterface;

import java.util.List;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.MyViewHolder> {

    Context context;
    List<Note> notes;
    private final NoteRecyclerViewInterface noteRecyclerViewInterface;


    public NoteAdapter(Context context, List<Note> notes, NoteRecyclerViewInterface noteRecyclerViewInterface) {
        this.context = context;
        this.notes = notes;
        this.noteRecyclerViewInterface = noteRecyclerViewInterface;

    }

    @NonNull
    @Override
    public NoteAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.recycler_view_row_note, parent, false);
        return new NoteAdapter.MyViewHolder(view, noteRecyclerViewInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteAdapter.MyViewHolder holder, int position) {

        String[] content = notes.get(position).getContent().split("\n");

        holder.title.setText(notes.get(position).getTitle());

        if (content.length > 1) {
            holder.content1.setText(content[0]);
            holder.content2.setText(content[1]);
        } else if (content.length == 1) {
            holder.content1.setText(content[0]);
            holder.content2.setVisibility(View.GONE);
        }


    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView title;
        TextView content1;
        TextView content2;
        ImageButton imageButton;

        public MyViewHolder(@NonNull View itemView, NoteRecyclerViewInterface noteRecyclerViewInterface) {
            super(itemView);

            title = itemView.findViewById(R.id.noteTitleRow);
            content1 = itemView.findViewById(R.id.noteContentFirstLine);
            content2 = itemView.findViewById(R.id.noteContentSecondLine);
            imageButton = itemView.findViewById(R.id.noteRowButton);

            imageButton.setOnClickListener(view -> {
                if (noteRecyclerViewInterface != null) {
                    int position = getAdapterPosition();

                    if (position != RecyclerView.NO_POSITION) {
                        noteRecyclerViewInterface.onNoteClick(position);
                    }
                }
            });

        }

    }

}