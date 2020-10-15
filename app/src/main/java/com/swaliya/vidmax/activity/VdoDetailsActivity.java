package com.swaliya.vidmax.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.MediaFormat;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.webkit.URLUtil;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.swaliya.vidmax.R;
import com.swaliya.vidmax.configg.Config;
import com.swaliya.vidmax.configg.SessionManager;

import java.util.Locale;

public class VdoDetailsActivity extends AppCompatActivity {
    SessionManager session;
    SharedPreferences pref;
    String loginid = "", mobilenumber = "", passwords = "";
    String item = "", itemFi = "";
    VideoView mVideoView;
    ImageView imgPlay, imgFull, imgSetting;
    private InterstitialAd mInterstitialAd;
    private static final String VIDEO_SAMPLE = "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerFun.mp4";

    //  private static final String VIDEO_SAMPLE = "https://developers.google.com/training/images/tacoma_narrows.mp4";
    private TextView mBufferingTextView;
    ProgressBar progressBar;

    // Current playback position (in milliseconds).
    private int mCurrentPosition = 0;

    // Tag for the instance state bundle.
    private static final String PLAYBACK_TIME = "play_time";
    public MediaController controller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vdodetails);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        findViewById(R.id.imgBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
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
        mVideoView = findViewById(R.id.vid);
        imgPlay = findViewById(R.id.omgPlay);
        imgFull = findViewById(R.id.imgFull);
        imgSetting = findViewById(R.id.imgSetting);
        mBufferingTextView = findViewById(R.id.buffering_textview);
        progressBar = findViewById(R.id.pBar);

        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-7999232318006976/4666901574");
        mInterstitialAd.loadAd(new AdRequest.Builder().build());


        if (savedInstanceState != null) {
            mCurrentPosition = savedInstanceState.getInt(PLAYBACK_TIME);
        }

        // Set up the media controller widget and attach it to the video view.

        controller = new MediaController(this);


        findViewById(R.id.imgShare).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                ConnectivityManager cn = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo nf = cn.getActiveNetworkInfo();
                if (nf != null && nf.isConnected() == true) {
                    if (mInterstitialAd.isLoaded()) {
                        mInterstitialAd.show();
                    } else {
                        Log.d("TAG", "The interstitial wasn't loaded yet.");
                        mInterstitialAd.loadAd(new AdRequest.Builder().build());
                    }
                    Toast.makeText(VdoDetailsActivity.this, "Share Some Details", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(VdoDetailsActivity.this, "Check internet connection", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }


    @Override
    protected void onStart() {
        super.onStart();

        imgFull.setVisibility(View.GONE);
        imgSetting.setVisibility(View.GONE);
        imgPlay.setVisibility(View.VISIBLE);
        imgPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ConnectivityManager cn = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo nf = cn.getActiveNetworkInfo();
                if (nf != null && nf.isConnected() == true) {
                    if (mInterstitialAd.isLoaded()) {
                        mInterstitialAd.show();
                    } else {
                        Log.d("TAG", "The interstitial wasn't loaded yet.");
                        mInterstitialAd.loadAd(new AdRequest.Builder().build());
                    }
                    progressBar.setVisibility(View.VISIBLE);
                    imgFull.setVisibility(View.VISIBLE);
                    imgSetting.setVisibility(View.VISIBLE);
                    controller.setMediaPlayer(mVideoView); //  MediaStore.EXTRA_VIDEO_QUALITY,720;

                    mVideoView.setMediaController(controller);
                    initializePlayer();
                    //  Toast.makeText(VdoDetailsActivity.this, "Playing vdo", Toast.LENGTH_SHORT).show();
                    imgPlay.setVisibility(View.GONE);
                } else {
                    Toast.makeText(VdoDetailsActivity.this, "Check internet connection", Toast.LENGTH_SHORT).show();
                }
            }
        });
        findViewById(R.id.imgSetting).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                PopupMenu popup = new PopupMenu(VdoDetailsActivity.this, view);
                //Inflating the Popup using xml file
                popup.getMenuInflater().inflate(R.menu.quality_menu, popup.getMenu());

                //registering popup with OnMenuItemClickListener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        Toast.makeText(VdoDetailsActivity.this, "Quality : " + item.getTitle(), Toast.LENGTH_SHORT).show();
                        return true;

                    }
                });

                popup.show();
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("TAG", "onResume: ");
        //  mVideoView.setAudioFocusRequest(AudioManager.AUDIOFOCUS_NONE);

        findViewById(R.id.imgFull).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), VdoPlayActivity.class).putExtra("key", VIDEO_SAMPLE).putExtra("volume", mVideoView.getCurrentPosition()));
                finish();
                Log.d("TAG", "onClick: " + mVideoView.getCurrentPosition());
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("TAG", "onPause: ");
        // In Android versions less than N (7.0, API 24), onPause() is the
        // end of the visual lifecycle of the app.  Pausing the video here
        // prevents the sound from continuing to play even after the app
        // disappears.
        //
        // This is not a problem for more recent versions of Android because
        // onStop() is now the end of the visual lifecycle, and that is where
        // most of the app teardown should take place.
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            mVideoView.pause();
        }
    }

    @Override
    protected void onStop() {

        super.onStop();
        Log.d("TAG", "onStop: ");
        // Media playback takes a lot of resources, so everything should be
        // stopped and released at this time.
        releasePlayer();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // Save the current playback position (in milliseconds) to the
        // instance state bundle.
        //    outState.putInt(PLAYBACK_TIME, mVideoView.getCurrentPosition());
    }

    private void initializePlayer() {
        // Show the "Buffering..." message while the video loads.
        //   mBufferingTextView.setVisibility(VideoView.VISIBLE);
        Uri uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.tuza);

        // Buffer and decode the video sample.
        Uri videoUri = getMedia(VIDEO_SAMPLE);
        mVideoView.setVideoURI(uri);

        Log.d("TAG", "initializePlayer: " + videoUri.toString());
        // Listener for onPrepared() event (runs after the media is prepared).

        mVideoView.setOnPreparedListener(
                new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mediaPlayer) {

                        // Hide buffering message.
                        mBufferingTextView.setVisibility(VideoView.INVISIBLE);

                        progressBar.setVisibility(View.GONE);
                        // Restore saved position, if available.
                        if (mCurrentPosition > 0) {
                            mVideoView.seekTo(mCurrentPosition);
                            mVideoView.start();
                        } else {
                            // Skipping to 1 shows the first frame of the video.
                            mVideoView.seekTo(1);
                            mVideoView.start();
                        }

                        // Start playing!

                        //   mediaPlayer.setVolume(1,15);
                    }
                });

        // Listener for onCompletion() event (runs after media has finished
        // playing).

        mVideoView.setOnCompletionListener(
                new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mediaPlayer) {
                        Toast.makeText(VdoDetailsActivity.this,
                                "setOnCompletionListener",
                                Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);

                        // Return the video position to the start.
                        mVideoView.seekTo(0);
                    }
                });
    }

    // Release all media-related resources. In a more complicated app this
    // might involve unregistering listeners or releasing audio focus.
    private void releasePlayer() {
        mVideoView.stopPlayback();
    }

    // Get a Uri for the media sample regardless of whether that sample is
    // embedded in the app resources or available on the internet.
    private Uri getMedia(String mediaName) {
        if (URLUtil.isValidUrl(mediaName)) {
            // Media name is an external URL.
            Log.d("TAG", "isValidUrl: " + mediaName);
            return Uri.parse(mediaName);
        } else {
            Log.d("TAG", "isValidNotUrl: " + mediaName);
            return Uri.parse("android.resource://" + getPackageName() +
                    "/raw/" + mediaName);
        }
    }


}
