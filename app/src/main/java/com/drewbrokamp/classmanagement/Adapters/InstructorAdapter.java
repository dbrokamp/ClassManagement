package com.drewbrokamp.classmanagement.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.drewbrokamp.classmanagement.Model.Instructor;
import com.drewbrokamp.classmanagement.Model.Term;
import com.drewbrokamp.classmanagement.R;
import com.drewbrokamp.classmanagement.Util.InstructorRecyclerViewInterface;
import com.drewbrokamp.classmanagement.Util.RecyclerViewInterface;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

public class InstructorAdapter extends RecyclerView.Adapter<InstructorAdapter.MyViewHolder> {

    Context context;
    List<Instructor> instructors;
    private final InstructorRecyclerViewInterface recyclerViewInterface;

    public InstructorAdapter(Context context, List<Instructor> instructors, InstructorRecyclerViewInterface recyclerViewInterface) {
        this.context = context;
        this.instructors = instructors;
        this.recyclerViewInterface = recyclerViewInterface;
    }

    @NonNull
    @Override
    public InstructorAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.recycler_view_row_instructor, parent, false);
        return new InstructorAdapter.MyViewHolder(view, recyclerViewInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull InstructorAdapter.MyViewHolder holder, int position) {

        holder.name.setText(instructors.get(position).getName());
        holder.email.setText(instructors.get(position).getEmail());
        holder.phoneNumber.setText(instructors.get(position).getPhoneNumber());

    }


    @Override
    public int getItemCount() {
        return instructors.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView name;
        TextView email;
        TextView phoneNumber;
        ImageButton imageButton;


        public MyViewHolder(@NonNull View itemView, InstructorRecyclerViewInterface recyclerViewInterface) {
            super(itemView);

            name = itemView.findViewById(R.id.instructorName);
            email = itemView.findViewById(R.id.instructorEmailRow);
            phoneNumber = itemView.findViewById(R.id.instructorPhoneNumberRow);
            imageButton = itemView.findViewById(R.id.instructorRowButton);


            imageButton.setOnClickListener(view -> {
                if (recyclerViewInterface != null) {
                    int position = getAdapterPosition();

                    if (position != RecyclerView.NO_POSITION) {
                        recyclerViewInterface.onInstructorClick(position);
                    }
                }
            });
        }

    }

}

