package com.swaliya.wowmax.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.VideoView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.swaliya.wowmax.R;
import com.swaliya.wowmax.configg.SessionManager;

import java.util.ArrayList;

import pl.droidsonroids.gif.GifImageView;


public class SplashActivity extends Activity {
    SessionManager session;
    Animation anim;
    ImageView imageView ;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        mAuth = FirebaseAuth.getInstance();

       /* if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                    | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }*/

        session = new SessionManager(getApplicationContext());
        imageView = (ImageView) findViewById(R.id.img);

        anim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.zoom); // Create the animation.
        imageView.startAnimation(anim);


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                /*if (mAuth.getCurrentUser() != null) {
                    finish();
                    startActivity(new Intent(SplashActivity.this, MainActivity.class).putExtra("key", "mail"));
                } else*/
                if (session.isLoggedIn() == true) {
                    startActivity(new Intent(getApplicationContext(), MainActivity.class).putExtra("key", "reg"));
                    finish();
                } else {
                    startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                    finish();
                }

            }
        }, 2000);

    }


}
