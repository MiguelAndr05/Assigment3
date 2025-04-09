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
import com.example.assignment3.ViewModel.SearchViewModel;

import java.util.List;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.SearchViewHolder> {

    private final Context context;
    private final List<MovieModel> movies;

    public SearchAdapter(Context context, List<MovieModel> movies, SearchViewModel searchViewModel) {
        this.context = context;
        this.movies = movies;
    }

    @NonNull
    @Override
    public SearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_movie, parent, false);
        return new SearchViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchViewHolder holder, int position) {
        MovieModel movie = movies.get(position);

        // Bind movie data to the views
        holder.title.setText(movie.getTitle());
        holder.year.setText(movie.getYearReleased());

        Glide.with(context)
            .load(movie.getMoviePosterURL())
            .placeholder(R.drawable.noimgfound)
            .into(holder.poster);

        // Handle item click
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, DetailedViewActivity.class);
            intent.putExtra("title", movie.getTitle());
            intent.putExtra("year", movie.getYearReleased());
            intent.putExtra("genres", movie.getGenres());
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
    /// ViewHolder class to hold the views for each movie item
    public static class SearchViewHolder extends RecyclerView.ViewHolder {
        TextView title, year;
        ImageView poster;

        public SearchViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.movieTitle);
            year = itemView.findViewById(R.id.movieYear);
            poster = itemView.findViewById(R.id.moviePoster);
        }
    }
}