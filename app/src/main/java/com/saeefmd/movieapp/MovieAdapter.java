package com.saeefmd.movieapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter {

    List<Result> movieList;
    Context context;

    public MovieAdapter(List<Result> movieList, Context context) {
        this.movieList = movieList;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(context)
                .inflate(R.layout.list_item_top_rated_movie, viewGroup, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {

        ((MyViewHolder)viewHolder).titleText.setText(movieList.get(i).getTitle());
        ((MyViewHolder)viewHolder).summaryText.setText(movieList.get(i).getOverview());
        ((MyViewHolder)viewHolder).ratingText.setText("Rating: " + movieList.get(i).getVoteAverage());

        Glide.with(context)
                .load("https://image.tmdb.org/t/p/w500" + movieList.get(i).getPosterPath())
                .into(((MyViewHolder)viewHolder).posterView);

        ((MyViewHolder)viewHolder).listItemLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });

    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView posterView;
        TextView titleText;
        TextView ratingText;
        TextView summaryText;

        LinearLayout listItemLayout;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            posterView = itemView.findViewById(R.id.poster);
            titleText = itemView.findViewById(R.id.title);
            summaryText = itemView.findViewById(R.id.summary);
            ratingText = itemView.findViewById(R.id.rating);

            listItemLayout = itemView.findViewById(R.id.list_item_layout);
        }
    }
}
