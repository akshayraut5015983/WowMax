package com.swaliya.wowmax.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.trackselection.MappingTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.swaliya.wowmax.R;
import com.swaliya.wowmax.adapter.DbAdapter;
import com.swaliya.wowmax.configg.Config;
import com.swaliya.wowmax.configg.SessionManager;
import com.swaliya.wowmax.helper.DatabaseHelper;
import com.swaliya.wowmax.model.DbModel;

import java.util.ArrayList;


public class SearchActivity extends AppCompatActivity {
    Button logoutBtn;

    SessionManager sessionManager;
    SharedPreferences pref;
    String loginid = "", mobilenumber = "", passwords = "";
    EditText edSearch;
    String strSearch = "";
    SimpleExoPlayerView exoPlayerView;
    SimpleExoPlayer exoPlayer;
    String videoURL = "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerFun.mp4";

    private TrackSelector trackSelector;
    private MappingTrackSelector selector;
    private TrackSelection.Factory videoTrackSelectionFactory;
    private DefaultBandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        findViewById(R.id.imgBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        final DatabaseHelper helper = new DatabaseHelper(SearchActivity.this);
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
        edSearch = findViewById(R.id.edSearch);
        findViewById(R.id.btnSearch).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                strSearch = edSearch.getText().toString().trim();
                edSearch.setText("");
                if (helper.insert(strSearch, strSearch + " Description")) {
                    Toast.makeText(SearchActivity.this, "Inserted", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(SearchActivity.this, "Not Insert", Toast.LENGTH_SHORT).show();
                }
                fillListview();
                //  Toast.makeText(SearchActivity.this, "Searching on " + strSearch, Toast.LENGTH_SHORT).show();


            }
        });


    }

    public void fillListview() {
        RecyclerView recyclerView = findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(SearchActivity.this));

        ArrayList<DbModel> dbModels = new ArrayList<>();
        DatabaseHelper dbhelper = new DatabaseHelper(this);

       /* ArrayList<DbModel> dogList = dbhelper.getAllData();
        for (int i = 0; i < dogList.size(); i++) {
            DbModel dbModel = new DbModel();
            dbModel.setId(dogList.get(i).getId());
            dbModel.setName(dogList.get(i).getName());
            dbModel.setDesp(dogList.get(i).getDesp());
            Log.d("TAG", "fillListview: " + dogList.get(i).getDesp() + dogList.get(i).getName());

        }*/

        DbAdapter dbAdapter = new DbAdapter(SearchActivity.this, dbhelper.getAllData());
        recyclerView.setAdapter(dbAdapter);
    /*    Customadapter myAdapter = new Customadapter(dogList, this);
        myListview.setAdapter(myAdapter);*/
    }

    @Override
    protected void onResume() {
        super.onResume();


    }


}
