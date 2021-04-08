package com.swaliya.wowmax.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.WindowManager;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.swaliya.wowmax.R;
import com.swaliya.wowmax.configg.Config;
import com.swaliya.wowmax.configg.SessionManager;

public class VideoVideoActivity extends AppCompatActivity implements ExoPlayer.EventListener {
    SimpleExoPlayer player;
    String path;        // it can be url of video for online streaming or a url of local video

    SessionManager session;
    SharedPreferences pref;
    String loginid = "", mobilenumber = "", passwords = "";
    String strLastUrl = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        session = new SessionManager(this);
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
        Bundle str = getIntent().getExtras();
        if (str != null) {
            strLastUrl = str.getString("key");
        }
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Log.e("TAG", "onCreate: " + policy.toString());
        Log.e("TAG", "on: " + strLastUrl);

        BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
        TrackSelection.Factory videoTrackSelectionFactory =
                new AdaptiveTrackSelection.Factory(bandwidthMeter);
        TrackSelector trackSelector =
                new DefaultTrackSelector(videoTrackSelectionFactory);

        // 2. Create a default LoadControl
        LoadControl loadControl = new DefaultLoadControl();
        // 3. Create the player
        player = ExoPlayerFactory.newSimpleInstance(this, trackSelector, loadControl);

        SimpleExoPlayerView playerView = (SimpleExoPlayerView) findViewById(R.id.exoplayer);
        playerView.setPlayer(player);
        playerView.setKeepScreenOn(true);
        // Produces DataSource instances through which media data is loaded.
        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(this, Util.getUserAgent(this, "ExoPlayer"));

        // Produces Extractor instances for parsing the media data.
        ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();

        // This is the MediaSource representing the media to be played.
        MediaSource videoSource = new ExtractorMediaSource(Uri.parse(strLastUrl),
                dataSourceFactory, extractorsFactory, null, null);
        // Prepare the player with the source.
        player.addListener(this);
        player.prepare(videoSource);
        playerView.requestFocus();
        player.setPlayWhenReady(true);      // to play video when ready. Use false to pause a video
    }


    @Override
    protected void onPause() {
        super.onPause();
        if (player != null) {
            player.setPlayWhenReady(false); //to pause a video because now our video player is not in focus
        }
    }


    @Override
    public void onTimelineChanged(Timeline timeline, int reason) {

    }

    @Override
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

    }

    @Override
    public void onLoadingChanged(boolean isLoading) {

    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        switch (playbackState) {
            case ExoPlayer.STATE_BUFFERING:
                //You can use progress dialog to show user that video is preparing or buffering so please wait
                break;
            case ExoPlayer.STATE_IDLE:
                //idle state
                break;
            case ExoPlayer.STATE_READY:
                // dismiss your dialog here because our video is ready to play now
                break;
            case ExoPlayer.STATE_ENDED:
                // do your processing after ending of video
                break;
        }
    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {
        // show user that something went wrong. I am showing dialog but you can use your way
        AlertDialog.Builder adb = new AlertDialog.Builder(VideoVideoActivity.this);
        adb.setTitle("Could not able to stream video");
        adb.setMessage("It seems that something is going wrong.\nPlease try again.");
        adb.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                finish(); // take out user from this activity. you can skip this
            }
        });
        AlertDialog ad = adb.create();
        ad.show();
    }

    @Override
    public void onPositionDiscontinuity(int reason) {
        Log.d("Mayur", "Discontinuity");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        player.release();   //it is important to release a player
    }
}
