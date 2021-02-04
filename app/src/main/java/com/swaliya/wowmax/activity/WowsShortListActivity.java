package com.swaliya.wowmax.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.swaliya.wowmax.R;
import com.swaliya.wowmax.adapter.WowShortAdapter;
import com.swaliya.wowmax.configg.Config;
import com.swaliya.wowmax.configg.SessionManager;
import com.swaliya.wowmax.model.MainMovieListModel;
import com.swaliya.wowmax.model.WowMeModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class WowsShortListActivity extends AppCompatActivity {


    SessionManager sessionManager;
    SharedPreferences pref;
    String loginid = "", mobilenumber = "", passwords = "";
    List<WowMeModel> videoItems;
    ViewPager2 videosViewPager;
    ProgressDialog loading;
    private static final int VIDEO_CAPTURE = 101;

    public static ProgressDialog createProgressDialog(Context context) {
        ProgressDialog dialog = new ProgressDialog(context);
        try {
            dialog.show();
        } catch (WindowManager.BadTokenException e) {

        }
        dialog.setCancelable(false);
        dialog.getWindow()
                .setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.progressdialog);
        // dialog.setMessage(Message);
        return dialog;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wowshort_list);
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
        videosViewPager = findViewById(R.id.viewPagerVideos);
        videoItems = new ArrayList<>();

        findViewById(R.id.tvFollowing).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(WowsShortListActivity.this, "Following", Toast.LENGTH_SHORT).show();
            }
        });
        findViewById(R.id.tvForYou).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(WowsShortListActivity.this, "For You", Toast.LENGTH_SHORT).show();
            }
        });
         StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Log.e("TAG", "onCreate: " + policy.toString());
        if (loading == null) {
            loading = createProgressDialog(this);
        }
        loading.show();
        getResponce();
       /* MainMovieListModel item2 = new MainMovieListModel();
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
        videosViewPager.setAdapter(new WowShortAdapter(videoItems, this));*/

        findViewById(R.id.layHome).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        findViewById(R.id.layWowSearch).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // startActivity(new Intent(VdoPlayingListActivity.this, VdoPlayingListActivity.class));

            }
        });
        findViewById(R.id.layAdd).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                intent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, 15);
                startActivityForResult(intent, VIDEO_CAPTURE);

            }
        });
        findViewById(R.id.layProfile).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(WowsShortListActivity.this, ProfileActivity.class));
                finish();
            }
        });

    }

    private void getResponce() {

        String url = Config.URL + "api/apiurl.aspx?msg=WowMeVideoList";
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                parseData(response);
                Log.d("TAG", "onnse: " + response);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loading.dismiss();
                //  Toast.makeText(getContext(), "Poor Internet Connection", Toast.LENGTH_LONG).show();
                Log.d("TAG", "onErrorResponse: " + error.getLocalizedMessage());
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonArrayRequest);

    }

    private void parseData(JSONArray response) {
        for (int i = 0; i < response.length(); i++) {
            WowMeModel mAinMoviListModel = new WowMeModel();

            try {
                JSONObject jsonObject = response.getJSONObject(i);


                mAinMoviListModel.setUserID(jsonObject.getString("UserID"));
                mAinMoviListModel.setUserName(jsonObject.getString("UserName"));
                mAinMoviListModel.setVideoName(jsonObject.getString("VideoName"));
                mAinMoviListModel.setDescription(jsonObject.getString("Description"));
                mAinMoviListModel.setHashTag(jsonObject.getString("HashTag"));
                mAinMoviListModel.setVidePath(jsonObject.getString("VidePath"));

            } catch (Exception e) {
                Log.d("TAG", "setListComedy: " + e);
            }
            loading.dismiss();
            videoItems.add(mAinMoviListModel);
            videosViewPager.setAdapter(new WowShortAdapter(videoItems, this));

        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            if (requestCode == VIDEO_CAPTURE) {
                if (resultCode == RESULT_OK) {
                    Uri videoUri = data.getData();
                    Log.e("TAG", "onActivityResult: " + data.getData());
                    Toast.makeText(this, "Video saved to:\n" +
                            videoUri, Toast.LENGTH_LONG).show();
                } else if (resultCode == RESULT_CANCELED) {
                    Toast.makeText(this, "Video recording cancelled.",
                            Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(this, "Failed to record video",
                            Toast.LENGTH_LONG).show();
                }
            }
        } else {
            Toast.makeText(this, "Video recording cancelled.", Toast.LENGTH_SHORT).show();
        }
    }


}