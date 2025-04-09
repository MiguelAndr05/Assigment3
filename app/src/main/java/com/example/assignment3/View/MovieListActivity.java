package com.example.assignment3.View;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.assignment3.Model.MovieListAdapter;
import com.example.assignment3.Model.MovieModel;
import com.example.assignment3.R;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class MovieListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private MovieListAdapter adapter;
    private List<MovieModel> movieList = new ArrayList<>();
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_list);

        db = FirebaseFirestore.getInstance();

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new MovieListAdapter(movieList, this::onMovieClicked);
        recyclerView.setAdapter(adapter);

        fetchMoviesFromFirestore();

        // Handle Add Button
        Button addButton = findViewById(R.id.addButton);
        addButton.setOnClickListener(v -> {
            // Navigate to the Movie Search page
            Intent intent = new Intent(MovieListActivity.this, SearchViewActivity.class);
            startActivity(intent);
        });
    }

    private void fetchMoviesFromFirestore() {
        db.collection("movies").get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    movieList.clear();
                    for (DocumentSnapshot document : queryDocumentSnapshots) {
                        MovieModel movie = document.toObject(MovieModel.class);
                        if (movie != null) {
                            movieList.add(movie);
                        }
                    }
                    adapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> Log.e("MovieListActivity", "Error fetching movies: " + e.getMessage()));
    }

    private void onMovieClicked(MovieModel movie) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose an action")
                .setItems(new String[]{"Edit", "Delete"}, (dialog, which) -> {
                    if (which == 0) {
                        // Edit the movie
                        Intent intent = new Intent(MovieListActivity.this, EditMovieActivity.class);
                        intent.putExtra("movieTitle", movie.getTitle());
                        startActivity(intent);
                    } else if (which == 1) {
                        // Delete the movie
                        deleteMovieByTitle(movie.getTitle());
                    }
                })
                .show();
    }

    private void deleteMovieByTitle(String title) {
        db.collection("movies")
                .whereEqualTo("title", title)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        for (DocumentSnapshot document : queryDocumentSnapshots) {
                            document.getReference().delete()
                                    .addOnSuccessListener(aVoid -> {
                                        Toast.makeText(this, "Movie deleted successfully", Toast.LENGTH_SHORT).show();
                                        fetchMoviesFromFirestore(); // Refresh the list
                                    })
                                    .addOnFailureListener(e -> {
                                        Toast.makeText(this, "Failed to delete movie", Toast.LENGTH_SHORT).show();
                                        Log.e("MovieListActivity", "Error deleting movie: " + e.getMessage());
                                    });
                        }
                    } else {
                        Toast.makeText(this, "No movie found with the title: " + title, Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> Log.e("MovieListActivity", "Error querying movie by title: " + e.getMessage()));
    }
}