package com.example.assignment3.Model;

public class MovieModel {

    private String id;
    private String title;
    private String genres;
    private String yearReleased;
    private String rating;
    private String description;
    private String moviePosterURL;

    public MovieModel(){};


    // The ID in this constructor doesnt work i was unable to get it to work/ pull from the api
    public MovieModel(String id, String title, String yearReleased, String moviePosterURL){
        this.id = id;
        this.title = title;
        this.yearReleased = yearReleased;
        this.moviePosterURL = moviePosterURL;
    }

    public MovieModel(String id,
                      String title,
                      String yearReleased,
                      String genres,
                      String moviePosterURL,
                      String rating,
                      String description) {

        this.id = id;
        this.title = title;
        this.yearReleased = yearReleased;
        this.genres = genres;
        this.moviePosterURL = moviePosterURL;
        this.rating = rating;
        this.description = description;
    }

    public MovieModel(String movieTitle, String movieYear, String movieGenres, String movieRating, String moviePoster, String movieDescription) {
    }

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getYearReleased() {
        return yearReleased;
    }
    public void setYearReleased(String yearReleased) {
        this.yearReleased = yearReleased;
    }
    public String getGenres() {
        return genres;
    }
    public void setGenres(String genres) {
        this.genres = genres;
    }
    public String getMoviePosterURL() {
        return moviePosterURL;
    }
    public void setMoviePosterURL(String moviePosterURL) {

        this.moviePosterURL = moviePosterURL;
    }
    public String getRating() {
        return rating;
    }
    public void setRating(String rating) {

        this.rating = rating;
    }
    public String getDescription() {

        return description;
    }
    public void setDescription(String description) {

        this.description = description;
    }
}
