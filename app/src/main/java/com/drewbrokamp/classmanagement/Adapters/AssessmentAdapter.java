package com.drewbrokamp.classmanagement.Adapters;

import android.content.Context;
import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.drewbrokamp.classmanagement.Model.Assessment;
import com.drewbrokamp.classmanagement.R;
import com.drewbrokamp.classmanagement.Util.AssessmentRecyclerViewInterface;
import com.drewbrokamp.classmanagement.Util.RecyclerViewInterface;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

public class AssessmentAdapter extends RecyclerView.Adapter<AssessmentAdapter.MyViewHolder> {

    Context context;
    List<Assessment> assessments;
    private final AssessmentRecyclerViewInterface recyclerViewInterface;


    public AssessmentAdapter(Context context, List<Assessment> assessments, AssessmentRecyclerViewInterface recyclerViewInterface) {
        this.context = context;
        this.assessments = assessments;
        this.recyclerViewInterface = recyclerViewInterface;

    }

    @NonNull
    @Override
    public AssessmentAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.recycler_view_row_assessment, parent, false);
        return new AssessmentAdapter.MyViewHolder(view, recyclerViewInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull AssessmentAdapter.MyViewHolder holder, int position) {

        holder.title.setText(assessments.get(position).getTitle());

        if (assessments.get(position).getType().equals("Objective")) {
            holder.assessmentTypeIcon.setImageTintList(ColorStateList.valueOf(ContextCompat.getColor(context, R.color.icon_green)));
            holder.assessmentType.setText(assessments.get(position).getType());
        } else {
            holder.assessmentTypeIcon.setImageResource(R.drawable.icon_performance);
            holder.assessmentTypeIcon.setImageTintList(ColorStateList.valueOf(ContextCompat.getColor(context, R.color.icon_yellow)));
            holder.assessmentType.setText(assessments.get(position).getType());
        }

        DateFormat dateFormat = new SimpleDateFormat("MM/dd/yy");
        String startDate = dateFormat.format(assessments.get(position).getStartDate());
        String endDate = dateFormat.format(assessments.get(position).getEndDate());
        String dates = startDate + " - " + endDate;
        holder.assessmentDates.setText(dates);

    }

    @Override
    public int getItemCount() {
        return assessments.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView title;
        AppCompatImageView assessmentTypeIcon;
        TextView assessmentType;
        TextView assessmentDates;
        ImageButton imageButton;

        public MyViewHolder(@NonNull View itemView, AssessmentRecyclerViewInterface recyclerViewInterface) {
            super(itemView);

            title = itemView.findViewById(R.id.assessmentTitle);
            assessmentTypeIcon = itemView.findViewById(R.id.assessmentTypeIcon);
            assessmentType = itemView.findViewById(R.id.assessmentType);
            assessmentDates = itemView.findViewById(R.id.assessmentDates);
            imageButton = itemView.findViewById(R.id.imageButtonRecyclerViewRowAssessmentArrow);

            imageButton.setOnClickListener(view -> {
                if (recyclerViewInterface != null) {
                    int position = getAdapterPosition();

                    if (position != RecyclerView.NO_POSITION) {
                        recyclerViewInterface.onAssessmentClick(position);
                    }
                }
            });

        }

    }

}