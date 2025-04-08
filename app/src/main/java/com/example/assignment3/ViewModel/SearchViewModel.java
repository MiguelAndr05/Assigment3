package com.example.assignment3.ViewModel;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.assignment3.Model.MovieModel;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class SearchViewModel extends ViewModel {

    private static final String API_URL = "https://www.omdbapi.com/?apikey=f047527a";
    private final MutableLiveData<List<MovieModel>> movieInfo = new MutableLiveData<>();
    private final MutableLiveData<MovieModel> movieDetails = new MutableLiveData<>();
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final OkHttpClient client = new OkHttpClient();
    private final List<MovieModel> movieList = new ArrayList<>();

    public LiveData<List<MovieModel>> getMovieInfo() {
        return movieInfo;
    }

    public LiveData<MovieModel> getMovieDetails() {
        return movieDetails;
    }

    // Search for movies using the OMDb API
    public void moviesSearch(String userQuery) {
        String urlWithQuery = API_URL + "&s=" + userQuery;

        Request request = new Request.Builder().url(urlWithQuery).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.e("SearchViewModel", "Failed to fetch movies: " + e.getMessage());
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.body() == null) {
                    Log.e("SearchViewModel", "Response body is null");
                    return;
                }
                String movieResponse = response.body().string();
                Log.d("SearchViewModel", "API Response: " + movieResponse); 
                try {
                    JSONObject jsonResponse = new JSONObject(movieResponse);
                    if (jsonResponse.has("Search")) {
                        JSONArray jsonArr = jsonResponse.getJSONArray("Search");
                        List<MovieModel> movieModelList = new ArrayList<>();
                        for (int i = 0; i < jsonArr.length(); i++) {
                            JSONObject objElement = jsonArr.getJSONObject(i);
                            String imdbID = objElement.optString("imdbID", "N/A");

                            // Fetch detailed information for each movie
                            fetchMovieDetails(imdbID, movieModelList);
                        }
                    } else {
                        Log.e("SearchViewModel", "No movies found in response");
                    }
                } catch (JSONException e) {
                    Log.e("SearchViewModel", "Error parsing movie data: " + e.getMessage());
                }
            }
        });
    }

    // Fetch detailed information about a specific movie
    public void specificMovieSearch(String movieId) {
        String idQueryUrl = API_URL + "&i=" + movieId;

        Request request = new Request.Builder().url(idQueryUrl).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.e("MovieViewModel", "Failed to fetch movie details: " + e.getMessage());
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.body() == null) return;
                String movieFullDetails = response.body().string();
                try {
                    JSONObject specificJsonResponse = new JSONObject(movieFullDetails);
                    MovieModel movieModelDetails = new MovieModel();
                    movieModelDetails.setId(specificJsonResponse.getString("imdbID"));
                    movieModelDetails.setTitle(specificJsonResponse.getString("Title"));
                    movieModelDetails.setYearReleased(specificJsonResponse.getString("Year"));
                    movieModelDetails.setGenres(specificJsonResponse.getString("Genre"));
                    movieModelDetails.setRating(specificJsonResponse.getString("imdbRating"));
                    movieModelDetails.setMoviePosterURL(specificJsonResponse.getString("Poster"));
                    movieModelDetails.setDescription(specificJsonResponse.getString("Plot"));
                    movieDetails.postValue(movieModelDetails);
                } catch (JSONException e) {
                    Log.e("MovieViewModel", "Error parsing movie details: " + e.getMessage());
                }
            }
        });
    }

    // Fetch detailed information for each movie and add to the list
    private void fetchMovieDetails(String imdbID, List<MovieModel> movieModelList) {
        if (imdbID == null || imdbID.isEmpty() || imdbID.equals("N/A")) {
            Log.e("SearchViewModel", "Skipping movie due to invalid imdbID");
            return;
        }

        String detailUrl = API_URL + "&i=" + imdbID;
        Log.d("SearchViewModel", "Detailed API URL: " + detailUrl);

        Request detailRequest = new Request.Builder().url(detailUrl).build();
        client.newCall(detailRequest).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.e("SearchViewModel", "Failed to fetch movie details: " + e.getMessage());
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.body() == null) {
                    Log.e("SearchViewModel", "Response body is null for movie details");
                    return;
                }
                String detailResponse = response.body().string();
                Log.d("SearchViewModel", "Detailed API Response: " + detailResponse);
                try {
                    JSONObject detailJson = new JSONObject(detailResponse);

                    if (detailJson.has("Response") && detailJson.getString("Response").equals("False")) {
                        Log.e("SearchViewModel", "API Error: " + detailJson.optString("Error", "Unknown error"));
                        return;
                    }

                    MovieModel movie = new MovieModel();
                    movie.setId(detailJson.optString("imdbID", "N/A")); // Set the ID here
                    movie.setTitle(detailJson.optString("Title", "N/A"));
                    movie.setYearReleased(detailJson.optString("Year", "N/A"));
                    movie.setGenres(detailJson.optString("Genre", "N/A"));
                    movie.setRating(detailJson.optString("imdbRating", "N/A"));
                    movie.setDescription(detailJson.optString("Plot", "N/A"));
                    movie.setMoviePosterURL(detailJson.optString("Poster", "N/A"));

                    // Add the movie to the list
                    movieModelList.add(movie);

                    // Post the updated list to LiveData
                    movieInfo.postValue(movieModelList);
                } catch (JSONException e) {
                    Log.e("SearchViewModel", "Error parsing movie details: " + e.getMessage());
                }
            }
        });
    }

    public void addMovieToFavorites(MovieModel movie) {
        db.collection("movies").add(movie)
                .addOnSuccessListener(documentReference -> Log.i("MovieViewModel", "Movie added to favorites"))
                .addOnFailureListener(e -> Log.e("MovieViewModel", "Failed to add movie to favorites: " + e.getMessage()));
    }

    public void fetchMoviesFromFavorites() {
        db.collection("movies").get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    movieList.clear();
                    for (DocumentSnapshot document : queryDocumentSnapshots) {
                        MovieModel movie = document.toObject(MovieModel.class);
                        if (movie != null) {
                            movie.setId(document.getId()); // Set the Firestore document ID
                            movieList.add(movie);
                        }
                    }
                    movieInfo.postValue(movieList); // Update LiveData
                    Log.i("SearchViewModel", "Movies fetched from favorites");
                })
                .addOnFailureListener(e -> Log.e("SearchViewModel", "Error fetching movies: " + e.getMessage()));
    }
}