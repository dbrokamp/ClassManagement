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

import com.drewbrokamp.classmanagement.R;
import com.drewbrokamp.classmanagement.Util.RecyclerViewInterface;

import java.util.List;

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.MyViewHolder> {

    Context context;
    List<String> titles;
    List<String> subTitles;
    int[] icons;
    private final RecyclerViewInterface recyclerViewInterface;


    public MainAdapter(Context context,int[] icons, List<String> titles, List<String> subTitles, RecyclerViewInterface recyclerViewInterface) {
        this.context = context;
        this.icons = icons;
        this.titles = titles;
        this.subTitles = subTitles;
        this.recyclerViewInterface = recyclerViewInterface;

    }

    @NonNull
    @Override
    public MainAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.recycler_view_row_main, parent, false);
        return new MainAdapter.MyViewHolder(view, recyclerViewInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull MainAdapter.MyViewHolder holder, int position) {

        holder.icon.setImageResource(icons[position]);
        holder.title.setText(titles.get(position));
        holder.subTitle.setText(subTitles.get(position));



    }

    @Override
    public int getItemCount() {
        return titles.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView icon;
        TextView title;
        TextView subTitle;
        ImageButton imageButton;


        public MyViewHolder(@NonNull View itemView, RecyclerViewInterface recyclerViewInterface) {
            super(itemView);

            icon = itemView.findViewById(R.id.imageViewCardViewMainLinksTitleIcon);
            title = itemView.findViewById(R.id.textViewCardViewMainLinksTitle);
            subTitle = itemView.findViewById(R.id.textViewCardViewMainLinksSubtitle);
            imageButton = itemView.findViewById(R.id.buttonCardViewMainLinks);


            imageButton.setOnClickListener(view -> {
                if (recyclerViewInterface != null) {
                    int position = getAdapterPosition();

                    if (position != RecyclerView.NO_POSITION) {
                        recyclerViewInterface.onItemClick(position);
                    }
                }
            });

        }

    }

}