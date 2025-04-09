package com.example.assignment3.View;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.assignment3.Model.MovieModel;
import com.example.assignment3.R;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class EditMovieActivity extends AppCompatActivity {

    private EditText titleEditText, genresEditText, yearEditText, ratingEditText, descriptionEditText;
    private Button saveButton;
    private FirebaseFirestore db;
    private String movieId; // Store the document ID

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        db = FirebaseFirestore.getInstance();

        titleEditText = findViewById(R.id.titleEditText);
        genresEditText = findViewById(R.id.genresEditText);
        yearEditText = findViewById(R.id.yearEditText);
        ratingEditText = findViewById(R.id.ratingEditText);
        descriptionEditText = findViewById(R.id.descriptionEditText);
        saveButton = findViewById(R.id.saveButton);
        Button cancelButton = findViewById(R.id.cancelButton);

        cancelButton.setOnClickListener(v -> {
            finish();
        });

        String movieTitle = getIntent().getStringExtra("movieTitle");
        if (movieTitle == null || movieTitle.isEmpty()) {
            Toast.makeText(this, "Movie title is missing. Cannot edit.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        fetchMovieDetailsByTitle(movieTitle);

        saveButton.setOnClickListener(v -> saveMovieDetails());
    }

    private void fetchMovieDetailsByTitle(String title) {
        db.collection("movies")
                .whereEqualTo("title", title)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        DocumentSnapshot document = queryDocumentSnapshots.getDocuments().get(0);
                        movieId = document.getId();
                        MovieModel movie = document.toObject(MovieModel.class);
                        if (movie != null) {
                            titleEditText.setText(movie.getTitle());
                            genresEditText.setText(movie.getGenres());
                            yearEditText.setText(movie.getYearReleased());
                            ratingEditText.setText(movie.getRating());
                            descriptionEditText.setText(movie.getDescription());
                        }
                    } else {
                        Toast.makeText(this, "No movie found with the title: " + title, Toast.LENGTH_SHORT).show();
                        finish();
                    }
                })
                .addOnFailureListener(e -> Toast.makeText(this, "Failed to fetch movie details", Toast.LENGTH_SHORT).show());
    }

    private void saveMovieDetails() {
        String title = titleEditText.getText().toString();
        String genres = genresEditText.getText().toString();
        String year = yearEditText.getText().toString();
        String rating = ratingEditText.getText().toString();
        String description = descriptionEditText.getText().toString();

        db.collection("movies").document(movieId) // Use the document ID retrieved earlier
                .update(
                        "title", title,
                        "genres", genres,
                        "yearReleased", year,
                        "rating", rating,
                        "description", description
                )
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Movie updated successfully", Toast.LENGTH_SHORT).show();
                    finish();
                })
                .addOnFailureListener(e -> Toast.makeText(this, "Failed to update movie", Toast.LENGTH_SHORT).show());
    }
}