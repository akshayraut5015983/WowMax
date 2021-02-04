package com.swaliya.wowmax.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
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

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.doubleclick.PublisherAdRequest;
import com.google.android.gms.ads.doubleclick.PublisherAdView;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdCallback;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;
import com.squareup.picasso.Picasso;
import com.swaliya.wowmax.BuildConfig;
import com.swaliya.wowmax.R;
import com.swaliya.wowmax.configg.Config;
import com.swaliya.wowmax.configg.SessionManager;

public class VdoDetailsActivity extends AppCompatActivity {
    SessionManager session;
    SharedPreferences pref;
    String loginid = "", mobilenumber = "", passwords = "";
    String item = "", itemFi = "";
    VideoView mVideoView;
    ImageView imgPlay, imgFull, imgSetting;

    private static String VIDEO_SAMPLE = "http://swaliyasoftech.com/img/video/swaliyaintro.mp4";
    private TextView mBufferingTextView;
    ProgressBar progressBar;
    private int mCurrentPosition = 0;
    private static final String PLAYBACK_TIME = "play_time";
    public MediaController controller;
    private ImageView imgPreview;
    String strName = "", strCat = "", strQul = "", strRel = "", strDur = "", strDesp = "", strurl = "", imgCode = "", strWrit = "", strMuscDire = "", strCast = "", strOneLine = "";

    TextView tvName, tvCat, tvqul, tvRel, tvDur, tvDesp, tvWriter, tvMusiceDir, tvCast, tvOneline;
    InterstitialAd mInterstitialAd;

    @SuppressLint("SetTextI18n")
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
        mInterstitialAd = new InterstitialAd(this);

        // set the ad unit ID
        mInterstitialAd.setAdUnitId(getString(R.string.intts_ads_unit));

        AdRequest adRequest = new AdRequest.Builder().build();

        // Load ads into Interstitial Ads
        mInterstitialAd.loadAd(adRequest);
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
        imgPreview = findViewById(R.id.imgPreview);
        tvName = findViewById(R.id.tvName);
        tvCat = findViewById(R.id.tvCat);
        tvqul = findViewById(R.id.tvQlt);
        tvRel = findViewById(R.id.tvRel);
        tvDur = findViewById(R.id.tvDur);
        tvDesp = findViewById(R.id.tvDesp);
        tvWriter = findViewById(R.id.tvWriter);
        tvMusiceDir = findViewById(R.id.tvMusicDirec);
        tvCast = findViewById(R.id.tvCast);
        tvOneline = findViewById(R.id.tvOneline);
        mVideoView = findViewById(R.id.vid);
        imgPlay = findViewById(R.id.omgPlay);
        imgFull = findViewById(R.id.imgFull);
        imgSetting = findViewById(R.id.imgSetting);
        mBufferingTextView = findViewById(R.id.buffering_textview);
        progressBar = findViewById(R.id.pBar);

        imgFull.setVisibility(View.GONE);
        imgSetting.setVisibility(View.GONE);
        imgPlay.setVisibility(View.VISIBLE);

        forAdvertise();
        Bundle i = getIntent().getExtras();
        if (i != null) {
            Log.e("TAG", "onCreate: " + i);
            strurl = i.getString("url");
            strName = i.getString("name");
            strCat = i.getString("cat");
            strRel = i.getString("rel");
            strQul = i.getString("qlt");
            strDur = i.getString("dur");
            strWrit = i.getString("writ");
            strMuscDire = i.getString("mdir");
            strCast = i.getString("cast");
            strOneLine = i.getString("onel");
            strDesp = i.getString("desp");
            imgCode = i.getString("img");

            //  imgPreview.setImageResource(imgCode);
            tvName.setText("Name- " + strName);
            tvCat.setText("Category- " + strCat);
            tvqul.setText("Quality -" + strQul);
            tvRel.setText("Release- " + strRel);
            tvDur.setText("Duration- " + strDur);
            tvWriter.setText("Writer- " + strWrit);
            tvMusiceDir.setText("MusicDirector -" + strMuscDire);
            tvCast.setText("Cast- " + strCast);
            tvOneline.setText("OneLine- " + strOneLine);
            tvDesp.setText("Description- " + strDesp);
            VIDEO_SAMPLE = strurl;
        }
        String imgUrl = Config.URL + imgCode;
        Log.d("TAG", "onCreate: " + strurl);
        Log.d("TAG", "onCreate img: " + imgUrl);

        Picasso.with(this)
                .load(imgUrl)
                .placeholder(R.drawable.black_wowmax_lan)
                .error(R.drawable.black_wowmax_lan)
                .into(imgPreview);

        if (savedInstanceState != null) {
            mCurrentPosition = savedInstanceState.getInt(PLAYBACK_TIME);
        }
        // Set up the media controller widget and attach it to the video view.

        controller = new MediaController(this);

        findViewById(R.id.tvTriler).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast toast = Toast.makeText(VdoDetailsActivity.this, "Coming Soon", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            }
        });
        findViewById(R.id.imgShare).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                ConnectivityManager cn = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo nf = cn.getActiveNetworkInfo();
                if (nf != null && nf.isConnected() == true) {

                    try {
                        Intent shareIntent = new Intent(Intent.ACTION_SEND);
                        shareIntent.setType("text/plain");
                        shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Wowmax");
                        String shareMessage = "\nLet me recommend you this application\n\n";

                        shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID + "\n\n";
                        shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                        startActivity(Intent.createChooser(shareIntent, "choose one"));
                    } catch (Exception e) {
                        Log.d("TAg", "onNavigationItemSelected: ");
                    }
                    //  Toast.makeText(VdoDetailsActivity.this, "Share Some Details", Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(VdoDetailsActivity.this, "Check internet connection", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }


    @Override
    protected void onStart() {
        super.onStart();

        imgPlay.setVisibility(View.VISIBLE);
        imgPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ConnectivityManager cn = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo nf = cn.getActiveNetworkInfo();
                if (nf != null && nf.isConnected() == true) {
                    /*if (mInterstitialAd.isLoaded()) {
                        mInterstitialAd.show();
                    } else {
                        Log.d("TAG", "The interstitial wasn't loaded yet.");
                        mInterstitialAd.loadAd(new AdRequest.Builder().build());
                    }
                    progressBar.setVisibility(View.VISIBLE);
                    imgFull.setVisibility(View.VISIBLE);
                    imgPreview.setVisibility(View.GONE);
                    imgSetting.setVisibility(View.VISIBLE);
                    controller.setMediaPlayer(mVideoView); //  MediaStore.EXTRA_VIDEO_QUALITY,720;

                    mVideoView.setMediaController(controller);
                    initializePlayer();
                    //  Toast.makeText(VdoDetailsActivity.this, "Playing vdo", Toast.LENGTH_SHORT).show();
                    imgPlay.setVisibility(View.GONE);*/

                    Log.e("TAG", "onClick: " + VIDEO_SAMPLE);
                    //   startActivity(new Intent(getApplicationContext(), VideoActivity.class).putExtra("key", VIDEO_SAMPLE));
                    Intent ii = new Intent(VdoDetailsActivity.this, VideoVideoActivity.class);
                    ii.putExtra("key", VIDEO_SAMPLE);
                    startActivity(ii);
                   /* Intent intent = new Intent(android.content.Intent.ACTION_VIEW);
                    Uri data = Uri.parse(VIDEO_SAMPLE);
                    intent.setData(data);
                    startActivity(intent);*/

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
      /*  mInterstitialAd.setAdListener(new AdListener() {
            public void onAdLoaded() {
                if (mInterstitialAd.isLoaded()) {
                    mInterstitialAd.show();
                }
            }
        });*/
        findViewById(R.id.imgFull).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), VdoPlayActivity.class).putExtra("key", VIDEO_SAMPLE).putExtra("volume", mVideoView.getCurrentPosition()));
                finish();
                Log.d("TAG", "onClick: " + mVideoView.getCurrentPosition());
            }
        });


    }

    private void forAdvertise() {
        MobileAds.initialize(VdoDetailsActivity.this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {

            }
        });
        PublisherAdView mPublisherAdView = findViewById(R.id.adView);
        PublisherAdRequest adRequest = new PublisherAdRequest.Builder().build();
        mPublisherAdView.loadAd(adRequest);

    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("TAG", "onPause: ");

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            mVideoView.stopPlayback();
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
        //       Uri uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.tuza);

        // Buffer and decode the video sample.
        Uri videoUri = getMedia(VIDEO_SAMPLE);
        mVideoView.setVideoURI(videoUri);

        Log.d("TAG", "initializePlayer: " + videoUri.toString());
        // Listener for onPrepared() event (runs after the media is prepared).

        mVideoView.setOnPreparedListener(
                new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mediaPlayer) {

                        // Hide buffering message.
                        mBufferingTextView.setVisibility(VideoView.INVISIBLE);
                        imgPlay.setVisibility(View.GONE);
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
                        imgPlay.setVisibility(View.VISIBLE);
                        // Return the video position to the start.
                        mVideoView.seekTo(1);
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
