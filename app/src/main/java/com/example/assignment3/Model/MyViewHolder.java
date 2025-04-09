package com.example.assignment3.Model;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.assignment3.R;

public class MyViewHolder extends RecyclerView.ViewHolder {
    TextView title, genres, rating, year;
    ImageView thumbnail;


    //
    public MyViewHolder(@NonNull View itemView) {
        super(itemView);
        title = itemView.findViewById(R.id.movieTitle);
        genres = itemView.findViewById(R.id.movieGenre);
        rating = itemView.findViewById(R.id.movieRating);
        year = itemView.findViewById(R.id.movieYear);
        thumbnail = itemView.findViewById(R.id.moviePoster);
    }
}