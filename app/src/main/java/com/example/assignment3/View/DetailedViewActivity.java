package com.example.assignment3.View;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.assignment3.Model.MovieModel;
import com.example.assignment3.R;
import com.google.firebase.firestore.FirebaseFirestore;

public class DetailedViewActivity extends AppCompatActivity {

    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_view);

        db = FirebaseFirestore.getInstance();

        // Find views by ID
        TextView title = findViewById(R.id.title);
        TextView genres = findViewById(R.id.genres);
        TextView year = findViewById(R.id.year);
        TextView rating = findViewById(R.id.rating);
        TextView description = findViewById(R.id.description);
        ImageView poster = findViewById(R.id.poster);
        Button favoriteBtn = findViewById(R.id.favoriteBtn);

        // Get data from Intent
        Intent intent = getIntent();
        String movieTitle = intent.getStringExtra("title");
        String movieGenres = intent.getStringExtra("genres");
        String movieYear = intent.getStringExtra("year");
        String movieRating = intent.getStringExtra("rating");
        String movieDescription = intent.getStringExtra("description");
        String moviePoster = intent.getStringExtra("poster");

        Button backButton = findViewById(R.id.backBtn);
        backButton.setOnClickListener(v -> finish());

        // Set data to views
        title.setText(movieTitle != null ? movieTitle : "N/A");
        genres.setText(movieGenres != null ? movieGenres : "N/A");
        year.setText(movieYear != null ? movieYear : "N/A");
        rating.setText(movieRating != null ? movieRating : "N/A");
        description.setText(movieDescription != null ? movieDescription : "N/A");

        Glide.with(this)
                .load(moviePoster != null ? moviePoster : R.drawable.noimgfound)
                .placeholder(R.drawable.noimgfound)
                .into(poster);

        // Handle favorite button click
        favoriteBtn.setOnClickListener(v -> {
            // Create a MovieModel object using the provided constructor
            MovieModel movie = new MovieModel(
                    null,
                    movieTitle != null ? movieTitle : "N/A",
                    movieYear != null ? movieYear : "N/A",
                    movieGenres != null ? movieGenres : "N/A",
                    moviePoster != null ? moviePoster : "N/A",
                    movieRating != null ? movieRating : "N/A",
                    movieDescription != null ? movieDescription : "N/A"
            );



            // Add the movie to Firestore
            db.collection("movies").add(movie)
                    .addOnSuccessListener(documentReference -> {
                        Toast.makeText(this, "Movie added to favorites", Toast.LENGTH_SHORT).show();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(this, "Failed to add movie to favorites", Toast.LENGTH_SHORT).show();
                    });
        });

    
        Button goToMovieListBtn = findViewById(R.id.goToMovieListBtn);

        // Set an OnClickListener to navigate to the movie list page
        goToMovieListBtn.setOnClickListener(v -> {
            Intent intentToMovieList = new Intent(DetailedViewActivity.this, MovieListActivity.class);
            startActivity(intentToMovieList);
        });
    }
}