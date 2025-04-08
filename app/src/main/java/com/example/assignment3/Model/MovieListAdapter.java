package com.example.assignment3.Model;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.assignment3.R;

import java.util.List;

public class MovieListAdapter extends RecyclerView.Adapter<MovieListAdapter.MovieViewHolder> {

    private final List<MovieModel> movieList;
    private final OnMovieClickListener listener;

    public interface OnMovieClickListener {
        void onMovieClick(MovieModel movie);
    }

    public MovieListAdapter(List<MovieModel> movieList, OnMovieClickListener listener) {
        this.movieList = movieList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_movie, parent, false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        MovieModel movie = movieList.get(position);
        holder.title.setText(movie.getTitle());
        holder.year.setText(movie.getYearReleased());
        holder.genre.setText(movie.getGenres());
        holder.rating.setText(movie.getRating());

        // Load the movie poster using Glide
        Glide.with(holder.itemView.getContext())
                .load(movie.getMoviePosterURL() != null ? movie.getMoviePosterURL() : R.drawable.noimgfound)
                .placeholder(R.drawable.noimgfound)
                .into(holder.poster);

        // Set click listener for the movie item
        holder.itemView.setOnClickListener(v -> listener.onMovieClick(movie));
    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }

    public static class MovieViewHolder extends RecyclerView.ViewHolder {
        TextView title, year, genre, rating;
        ImageView poster;

        public MovieViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.movieTitle);
            year = itemView.findViewById(R.id.movieYear);
            genre = itemView.findViewById(R.id.movieGenre);
            rating = itemView.findViewById(R.id.movieRating);
            poster = itemView.findViewById(R.id.moviePoster);
        }
    }
}