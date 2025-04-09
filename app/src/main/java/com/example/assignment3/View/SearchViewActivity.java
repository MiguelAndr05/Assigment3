package com.example.assignment3.View;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.assignment3.Model.MovieModel;
import com.example.assignment3.R;
import com.example.assignment3.Model.SearchAdapter;
import com.example.assignment3.ViewModel.SearchViewModel;

import java.util.ArrayList;
import java.util.List;

public class SearchViewActivity extends AppCompatActivity {

    private EditText searchField;
    private RecyclerView searchResultsRecyclerView;
    private SearchAdapter searchAdapter;
    private List<MovieModel> searchResults;
    private SearchViewModel searchViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        // Initialize ViewModel
        searchViewModel = new ViewModelProvider(this).get(SearchViewModel.class);

        // Find views by ID (Doesnt work still have zero idea why believe its an api issue)
        searchField = findViewById(R.id.searchBar);
        searchResultsRecyclerView = findViewById(R.id.searchResultsRecycler);
        Button searchBtn = findViewById(R.id.searchBtn);

        // Initialize RecyclerView
        searchResults = new ArrayList<>();
        searchAdapter = new SearchAdapter(this, searchResults, searchViewModel);
        searchResultsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        searchResultsRecyclerView.setAdapter(searchAdapter);

        // Observe LiveData from ViewModel
        searchViewModel.getMovieInfo().observe(this, movies -> {
            if (movies != null && !movies.isEmpty()) {
                searchResults.clear();
                searchResults.addAll(movies);
                searchAdapter.notifyDataSetChanged();
            } else {
                Toast.makeText(this, "No movies found", Toast.LENGTH_SHORT).show();
            }
        });

        // Handles search button click
        searchBtn.setOnClickListener(v -> {
            String query = searchField.getText().toString().trim();
            if (!query.isEmpty()) {
                searchViewModel.moviesSearch(query);
            } else {
                Toast.makeText(this, "Please enter a search query", Toast.LENGTH_SHORT).show();
            }
        });

        // Handles "Enter" key press in the search bar
        searchField.setOnEditorActionListener((v, actionId, event) -> {
            String query = searchField.getText().toString().trim();
            if (!query.isEmpty()) {
                searchViewModel.moviesSearch(query);
            } else {
                Toast.makeText(this, "Please enter a search query", Toast.LENGTH_SHORT).show();
            }
            return true;
        });
    }
}