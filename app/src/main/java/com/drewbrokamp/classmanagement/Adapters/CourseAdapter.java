package com.drewbrokamp.classmanagement.Adapters;

import android.content.Context;
import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;


import com.drewbrokamp.classmanagement.DAO.DBHelper;
import com.drewbrokamp.classmanagement.Model.Course;
import com.drewbrokamp.classmanagement.Model.Instructor;
import com.drewbrokamp.classmanagement.Model.Term;
import com.drewbrokamp.classmanagement.R;
import com.drewbrokamp.classmanagement.SearchActivity;
import com.drewbrokamp.classmanagement.Util.CourseRecyclerViewInterface;
import com.drewbrokamp.classmanagement.Util.RecyclerViewInterface;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

public class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.MyViewHolder> {

    Context context;
    List<Course> courses;
    private final CourseRecyclerViewInterface recyclerViewInterface;
    private final DBHelper dbHelper;

    public CourseAdapter(Context context, List<Course> courses, CourseRecyclerViewInterface recyclerViewInterface, DBHelper dbHelper) {
        this.context = context;
        this.courses = courses;
        this.recyclerViewInterface = recyclerViewInterface;
        this.dbHelper = dbHelper;
    }


    @NonNull
    @Override
    public CourseAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.recycler_view_row_course, parent, false);
        return new CourseAdapter.MyViewHolder(view, recyclerViewInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull CourseAdapter.MyViewHolder holder, int position) {


        holder.title.setText(courses.get(position).getTitle());

        switch (courses.get(position).getStatus()) {
            case ("Plan To Take"):
                holder.courseStatusIcon.setImageTintList(ColorStateList.valueOf(ContextCompat.getColor(context, R.color.main_blue)));
                holder.courseStatus.setText("Plan To Take");
                break;
            case ("In Progress"):
                holder.courseStatusIcon.setImageTintList(ColorStateList.valueOf(ContextCompat.getColor(context, R.color.icon_yellow)));
                holder.courseStatus.setText("In Progress");
                break;
            case ("Completed"):
                holder.courseStatusIcon.setImageTintList(ColorStateList.valueOf(ContextCompat.getColor(context, R.color.icon_green)));
                holder.courseStatus.setText("Completed");
                break;
            case ("Dropped"):
                holder.courseStatusIcon.setImageTintList(ColorStateList.valueOf(ContextCompat.getColor(context, R.color.icon_red)));
                holder.courseStatus.setText("Dropped");
                break;
        }

        DateFormat dateFormat = new SimpleDateFormat("MM/dd/yy");
        String startDate = dateFormat.format(courses.get(position).getStartDate());
        String endDate = dateFormat.format(courses.get(position).getEndDate());
        String dates = startDate + " - " + endDate;
        holder.courseDates.setText(dates);



    }


    @Override
    public int getItemCount() {
        return courses.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView title;
        AppCompatImageView courseStatusIcon;
        TextView courseStatus;
        TextView courseDates;
        ImageButton imageButton;



        public MyViewHolder(@NonNull View itemView, CourseRecyclerViewInterface recyclerViewInterface) {
            super(itemView);

            title = itemView.findViewById(R.id.courseName);
            courseStatusIcon = itemView.findViewById(R.id.imageViewRecyclerViewRowCourseStarIcon);
            courseStatus = itemView.findViewById(R.id.courseStatus);
            courseDates = itemView.findViewById(R.id.courseDates);
            imageButton = itemView.findViewById(R.id.courseDetailViewButton);

            imageButton.setOnClickListener(view -> {
                if (recyclerViewInterface != null) {
                    int position = getAdapterPosition();

                    if (position != RecyclerView.NO_POSITION) {
                        recyclerViewInterface.onCourseClick(position);
                    }
                }
            });
        }

    }

}