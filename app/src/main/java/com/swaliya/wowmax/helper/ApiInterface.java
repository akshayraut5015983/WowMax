package com.swaliya.wowmax.helper;

import com.swaliya.wowmax.model.MainMovieListModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiInterface {
    // volley_array.json    for testing
    @GET("api/apiurl.aspx?msg=GetMovieDetails")
    Call<List<MainMovieListModel>> getMovies();
}
