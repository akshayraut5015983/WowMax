package com.swaliya.vidmax.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.swaliya.vidmax.R;
import com.swaliya.vidmax.configg.Config;
import com.swaliya.vidmax.configg.SessionManager;


public class WishListActivity extends AppCompatActivity {


    SessionManager sessionManager;
    SharedPreferences pref;
    String loginid = "", mobilenumber = "", passwords = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wishlist);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        findViewById(R.id.imgBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
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

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}