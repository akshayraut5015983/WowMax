package com.swaliya.wowmax.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.swaliya.wowmax.R;
import com.swaliya.wowmax.configg.Config;
import com.swaliya.wowmax.configg.SessionManager;

public class ViewAllVdoActivity extends AppCompatActivity  {
    SessionManager session;
    SharedPreferences pref;
    String loginid = "", mobilenumber = "", passwords = "";
    String item = "", itemFi = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_all_vdo);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        findViewById(R.id.imgBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        session = new SessionManager(this);
      //  https://gist.github.com/jsturgis/3b19447b304616f18657
        session = new SessionManager(getApplicationContext());
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
        findViewById(R.id.lo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // startActivity(new Intent(getApplicationContext(),VideoActivity.class));
            }
        });

    }




}
