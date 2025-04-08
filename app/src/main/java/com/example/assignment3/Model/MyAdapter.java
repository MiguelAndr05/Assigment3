package com.example.assignment3.Model;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.assignment3.R;
import com.example.assignment3.View.DetailedViewActivity;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    private final List<MovieModel> movies;
    private final Context context;

    public MyAdapter(Context context, List<MovieModel> movies) {
        this.context = context;
        this.movies = movies;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View movieView = LayoutInflater.from(context).inflate(R.layout.item_movie, parent, false);
        return new MyViewHolder(movieView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        MovieModel movie = movies.get(position);

        // Bind movie data to the views
        holder.title.setText(movie.getTitle());
        holder.genres.setText(movie.getGenres());
        holder.rating.setText("Rating: " + movie.getRating());
        holder.year.setText("Year: " + movie.getYearReleased());

        // Load the movie poster using Glide
        Glide.with(context)
                .load(movie.getMoviePosterURL())
                .placeholder(R.drawable.noimgfound)
                .into(holder.thumbnail);

        // Handle item click
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, DetailedViewActivity.class);
            intent.putExtra("title", movie.getTitle());
            intent.putExtra("genres", movie.getGenres());
            intent.putExtra("year", movie.getYearReleased());
            intent.putExtra("rating", movie.getRating());
            intent.putExtra("description", movie.getDescription());
            intent.putExtra("poster", movie.getMoviePosterURL());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    // ViewHolder class to hold references to the views in each item
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView title, genres, rating, year;
        ImageView thumbnail;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.movieTitle);
            genres = itemView.findViewById(R.id.movieGenre);
            rating = itemView.findViewById(R.id.movieRating);
            year = itemView.findViewById(R.id.movieYear);
            thumbnail = itemView.findViewById(R.id.moviePoster);
        }
    }
}