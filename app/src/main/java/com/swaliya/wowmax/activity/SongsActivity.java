package com.swaliya.wowmax.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.webkit.URLUtil;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.swaliya.wowmax.R;
import com.swaliya.wowmax.adapter.SongsAdapter;
import com.swaliya.wowmax.adapter.WowShortAdapter;
import com.swaliya.wowmax.configg.Config;
import com.swaliya.wowmax.configg.SessionManager;
import com.swaliya.wowmax.model.MainMovieListModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class SongsActivity extends AppCompatActivity {


    SessionManager sessionManager;
    SharedPreferences pref;
    String loginid = "", mobilenumber = "", passwords = "";


    List<MainMovieListModel> videoItems;

    ProgressDialog loading;
    VideoView mVideoView;
    RecyclerView recyclerView;
    SongsAdapter songsAdapter;
    androidx.appcompat.widget.SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_songs);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        sessionManager = new SessionManager(this);
        pref = getSharedPreferences(Config.PREF_NAME, Context.MODE_PRIVATE);
        if (pref.contains(Config.KEY_NAME)) {
            loginid = pref.getString(Config.KEY_NAME, "");
        }
        if (pref.contains(Config.KEY_MOBILE)) {
            mobilenumber = pref.getString(Config.KEY_MOBILE, "");
        }
        if (pref.contains(Config.KEY_PASSWORD)) {
            passwords = pref.getString(Config.KEY_PASSWORD, "");
        }

        mVideoView = findViewById(R.id.vid);
        recyclerView = findViewById(R.id.recycler);
        searchView = findViewById(R.id.search);
        videoItems = new ArrayList<>();
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

            //    Toast.makeText(SongsActivity.this,  query, Toast.LENGTH_LONG).show();

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // Toast.makeText(SongsActivity.this, newText+"A", Toast.LENGTH_SHORT).show();
                return false;
            }
        });

        //  loading = ProgressDialog.show(this, "", "Please Wait...", false, false);


        getResponce();
      /*  MainMovieListModel item2 = new MainMovieListModel();
        item2.setMovieAddress("http://wowmaxmovies.com/video/paijantrailer.mp4");
        item2.setMovieTitle("Sasha Solomon");
        item2.setMovieDesc("How Sasha Solomon Became a Software Developer at Twitter");
        videoItems.add(item2);

        MainMovieListModel item = new MainMovieListModel();
        item.setMovieAddress("http://wowmaxmovies.com/video/paijantrailer.mp4");
        item.setMovieTitle("The Key OF Life");
        item.setMovieDesc("International Women's Day 2019");
        videoItems.add(item);


        MainMovieListModel item3 = new MainMovieListModel();
        item3.setMovieAddress("http://wowmaxmovies.com/video/paijantrailer.mp4");
        item3.setMovieTitle("Happy Hour Wednesday");
        item3.setMovieDesc("Depth-First Search Algorithm");
        videoItems.add(item3);
        // videosViewPager.setAdapter(new VideosAdapter(videoItems, this));
*/
        findViewById(R.id.layHome).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        initializePlayer("");


    }

    public void initiaPlayer(String s) {
        Toast.makeText(SongsActivity.this, s, Toast.LENGTH_SHORT).show();
    }

    protected void initializePlayer(String s) {
        Uri videoUri = Uri.parse("http://wowmaxmovies.com/video/paijantrailer.mp4");
        MediaController mediaController = new MediaController(this);
        mediaController.setAnchorView(mVideoView);
        mVideoView.setMediaController(mediaController);
        mVideoView.setVideoURI(videoUri);

        mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {

                //  mp.start();
                mp.seekTo(0);

            }
        });
        mVideoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mp.seekTo(0);
            }
        });
    }
    private void getResponce() {

        String url = Config.URL + "api/apiurl.aspx?msg=GetMovieDetails";
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                parseData(response);
                Log.d("TAG", "onnse: " + response);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //  Toast.makeText(getContext(), "Poor Internet Connection", Toast.LENGTH_LONG).show();
                Log.d("TAG", "onErrorResponse: " + error.getLocalizedMessage());
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonArrayRequest);

    }

    private void parseData(JSONArray response) {
        for (int i = 0; i < response.length(); i++) {
            MainMovieListModel mAinMoviListModel = new MainMovieListModel();

            try {
                JSONObject jsonObject = response.getJSONObject(i);
                mAinMoviListModel.setMovieTitle(jsonObject.getString("MovieTitle"));
                mAinMoviListModel.setCategoryName(jsonObject.getString("CategoryName"));
                mAinMoviListModel.setMovieQuality(jsonObject.getString("MovieQuality"));
                mAinMoviListModel.setMovieImage(jsonObject.getString("MovieImage"));
                mAinMoviListModel.setMovieDesc(jsonObject.getString("MovieDesc"));
                mAinMoviListModel.setMovieRelYear(jsonObject.getString("MovieRelYear"));
                mAinMoviListModel.setMovieLanguage(jsonObject.getString("MovieLanguage"));
                mAinMoviListModel.setMovieDuration(jsonObject.getString("MovieDuration"));
                mAinMoviListModel.setMovieAddress(jsonObject.getString("MovieAddress"));
                mAinMoviListModel.setMovieImage2(jsonObject.getString("MovieImage2"));

            } catch (Exception e) {
                Log.d("TAG", "setListComedy: " + e);
            }
            videoItems.add(mAinMoviListModel);
            songsAdapter = new SongsAdapter(videoItems, this);
            recyclerView.setAdapter(songsAdapter);
        }

    }


    private boolean hasCamera() {
        return (getPackageManager().hasSystemFeature(
                PackageManager.FEATURE_CAMERA_FRONT));
    }

}