package com.drewbrokamp.classmanagement.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.drewbrokamp.classmanagement.Model.Term;
import com.drewbrokamp.classmanagement.R;
import com.drewbrokamp.classmanagement.Util.RecyclerViewInterface;
import com.drewbrokamp.classmanagement.Util.TermRecyclerViewInterface;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class TermAdapter extends RecyclerView.Adapter<TermAdapter.MyViewHolder> {

    Context context;
    List<Term> terms;
    private final TermRecyclerViewInterface recyclerViewInterface;

    public TermAdapter(Context context, List<Term> terms, TermRecyclerViewInterface recyclerViewInterface) {
        this.context = context;
        this.terms = terms;
        this.recyclerViewInterface = recyclerViewInterface;
    }

    @NonNull
    @Override
    public TermAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.recycler_view_row_term, parent, false);
        return new TermAdapter.MyViewHolder(view, recyclerViewInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull TermAdapter.MyViewHolder holder, int position) {

        DateFormat dateFormat = new SimpleDateFormat("MM/dd/yy", Locale.US);
        String startDate = dateFormat.format(terms.get(position).getStartDate());
        String endDate = dateFormat.format(terms.get(position).getEndDate());
        String dateSubtitle = startDate + " - " + endDate;

        holder.titleIcon.setImageResource(R.drawable.icon_term);
        holder.title.setText(terms.get(position).getTitle());
        holder.subTitleIcon.setImageResource(R.drawable.icon_calendar);
        holder.subTitle.setText(dateSubtitle);
        holder.imageButton.setImageResource(R.drawable.arrow_circle_right);



    }


    @Override
    public int getItemCount() {
        return terms.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView titleIcon;
        TextView title;
        ImageView subTitleIcon;
        TextView subTitle;
        ImageButton imageButton;




        public MyViewHolder(@NonNull View itemView, TermRecyclerViewInterface recyclerViewInterface) {
            super(itemView);

            titleIcon = itemView.findViewById(R.id.imageViewRecyclerViewRowTermTitleIcon);
            title = itemView.findViewById(R.id.textViewRecyclerViewRowTermTitle);
            subTitleIcon = itemView.findViewById(R.id.imageViewRecyclerViewRowTermSubtitleIcon);
            subTitle = itemView.findViewById(R.id.textViewRecyclerViewRowTermSubtitle);
            imageButton = itemView.findViewById(R.id.imageButtonRecyclerViewRowTerm);

            imageButton.setOnClickListener(view -> {
                if (recyclerViewInterface != null) {
                    int position = getAdapterPosition();

                    if (position != RecyclerView.NO_POSITION) {
                        recyclerViewInterface.onTermClick(position);
                    }
                }
            });

        }

    }

}
