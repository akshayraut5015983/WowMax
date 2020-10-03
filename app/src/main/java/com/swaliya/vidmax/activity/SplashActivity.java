package com.swaliya.vidmax.activity;

import android.animation.ValueAnimator;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.VideoView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.swaliya.vidmax.R;
import com.swaliya.vidmax.configg.SessionManager;

import java.util.ArrayList;


public class SplashActivity extends AppCompatActivity implements MediaPlayer.OnCompletionListener {
    SessionManager session;
    Animation anim;
    ImageView imageView, eimg, mimg;
    ImageView mImg, eImg;
    VideoView vw;
    ArrayList<Integer> videolist = new ArrayList<>();
    int currvideo = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                    | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }

        session = new SessionManager(getApplicationContext());
        imageView = (ImageView) findViewById(R.id.img);
        mimg = (ImageView) findViewById(R.id.mimg);
        eimg = (ImageView) findViewById(R.id.eimg);// Declare an imageView to show the animation.
        anim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.zoom); // Create the animation.
        imageView.startAnimation(anim);
        eImg = findViewById(R.id.eimg);
        mImg = findViewById(R.id.mimg);

        /*final ValueAnimator animator = ValueAnimator.ofFloat(0.0f, 1.0f);
        animator.setRepeatCount(ValueAnimator.INFINITE);
        animator.setInterpolator(new LinearInterpolator());
        animator.setDuration(2000L);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                final float progress = (float) animation.getAnimatedValue();
                final float width = eImg.getWidth();
                final float translationX = width * progress;
                eImg.setTranslationX(translationX);
                mImg.setTranslationX(translationX - width);
            }
        });
        animator.start();*/

        Animation animation1 =
                AnimationUtils.loadAnimation(getApplicationContext(),
                        R.anim.blink);
        Animation animation =
                AnimationUtils.loadAnimation(getApplicationContext(),
                        R.anim.fade);

        // imageView.startAnimation(animation1);
        mimg.startAnimation(animation1);
        eimg.startAnimation(animation1);


        //  session = new SessionManager(getApplicationContext());
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

               /* if (session.isLoggedIn() == true) {
                    Intent i = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(i);
                    //  overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                    finish();
                } else {*/
                Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                startActivity(intent);
                //  overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                finish();


            }
        }, 2000);
        /*vw = (VideoView) findViewById(R.id.vv);

        vw.setMediaController(new MediaController(this));
        vw.setOnCompletionListener(this);

        // video name should be in lower case alphabet.
        videolist.add(R.raw.vdo_spl);
      *//*  videolist.add(R.raw.faded);
        videolist.add(R.raw.aeroplane);*//*
        setVideo(videolist.get(0));*/
    }

    public void setVideo(int id) {
        String uriPath
                = "android.resource://"
                + getPackageName() + "/" + id;
        Uri uri = Uri.parse(uriPath);
        vw.setVideoURI(uri);
        vw.start();
    }

    public void onCompletion(MediaPlayer mediapalyer) {
        AlertDialog.Builder obj = new AlertDialog.Builder(SplashActivity.this);
        obj.setTitle("Playback Finished!");
        obj.setIcon(R.mipmap.ic_launcher);
        MyListener m = new MyListener();
        obj.setPositiveButton("Replay", m);
        obj.setNegativeButton("Next", m);
        obj.setMessage("Want to replay or play next video?");
        obj.show();
    }

    class MyListener implements DialogInterface.OnClickListener {
        public void onClick(DialogInterface dialog, int which) {
            if (which == -1) {
                vw.seekTo(0);
                vw.start();
            } else {
                ++currvideo;
                if (currvideo == videolist.size())
                    currvideo = 0;
                setVideo(videolist.get(currvideo));
            }
        }
    }


}
