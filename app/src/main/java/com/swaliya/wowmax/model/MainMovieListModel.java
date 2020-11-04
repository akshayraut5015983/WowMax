package com.swaliya.wowmax.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MainMovieListModel {

    @SerializedName("MovieTitle")
    @Expose
    private String movieTitle;
    @SerializedName("CategoryName")
    @Expose
    private String categoryName;
    @SerializedName("MovieQuality")
    @Expose
    private String movieQuality;
    @SerializedName("MovieImage")
    @Expose
    private String movieImage;
    @SerializedName("MovieDesc")
    @Expose
    private String movieDesc;
    @SerializedName("MovieRelYear")
    @Expose
    private String movieRelYear;
    @SerializedName("MovieLanguage")
    @Expose
    private String movieLanguage;
    @SerializedName("MovieDuration")
    @Expose
    private String movieDuration;
    @SerializedName("MoviePath")
    @Expose
    private String moviePath;
    @SerializedName("MovieName")
    @Expose
    private String movieName;
    @SerializedName("MovieAddress")
    @Expose
    private String movieAddress;
    @SerializedName("MovieImage2")
    @Expose
    private String movieImage2;

    public String getMovieTitle() {
        return movieTitle;
    }

    public void setMovieTitle(String movieTitle) {
        this.movieTitle = movieTitle;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getMovieQuality() {
        return movieQuality;
    }

    public void setMovieQuality(String movieQuality) {
        this.movieQuality = movieQuality;
    }

    public String getMovieImage() {
        return movieImage;
    }

    public void setMovieImage(String movieImage) {
        this.movieImage = movieImage;
    }

    public String getMovieDesc() {
        return movieDesc;
    }

    public void setMovieDesc(String movieDesc) {
        this.movieDesc = movieDesc;
    }

    public String getMovieRelYear() {
        return movieRelYear;
    }

    public void setMovieRelYear(String movieRelYear) {
        this.movieRelYear = movieRelYear;
    }

    public String getMovieLanguage() {
        return movieLanguage;
    }

    public void setMovieLanguage(String movieLanguage) {
        this.movieLanguage = movieLanguage;
    }

    public String getMovieDuration() {
        return movieDuration;
    }

    public void setMovieDuration(String movieDuration) {
        this.movieDuration = movieDuration;
    }

    public String getMoviePath() {
        return moviePath;
    }

    public void setMoviePath(String moviePath) {
        this.moviePath = moviePath;
    }

    public String getMovieName() {
        return movieName;
    }

    public void setMovieName(String movieName) {
        this.movieName = movieName;
    }

    public String getMovieAddress() {
        return movieAddress;
    }

    public void setMovieAddress(String movieAddress) {
        this.movieAddress = movieAddress;
    }

    public String getMovieImage2() {
        return movieImage2;
    }

    public void setMovieImage2(String movieImage2) {
        this.movieImage2 = movieImage2;
    }
}
