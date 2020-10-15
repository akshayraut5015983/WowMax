package com.swaliya.vidmax.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.media.MediaFormat;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.hls.HlsMediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.MappingTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectorResult;
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout;
import com.google.android.exoplayer2.ui.PlaybackControlView;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.ui.TrackSelectionView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.util.Assertions;
import com.google.android.exoplayer2.util.MimeTypes;
import com.google.android.exoplayer2.util.Util;
import com.swaliya.vidmax.R;
import com.swaliya.vidmax.configg.Config;
import com.swaliya.vidmax.configg.SessionManager;


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
                Toast.makeText(SearchActivity.this, "Searching on " + strSearch, Toast.LENGTH_SHORT).show();
            }
        });

        /*exoPlayerView = (SimpleExoPlayerView) findViewById(R.id.exo_player_view);

        try {

            bandwidthMeter = new DefaultBandwidthMeter();
            trackSelector = new DefaultTrackSelector(new AdaptiveTrackSelection.Factory(bandwidthMeter));

            exoPlayer = ExoPlayerFactory.newSimpleInstance(this, trackSelector);


            Uri videoURI = Uri.parse(videoURL);

            DefaultHttpDataSourceFactory dataSourceFactory = new DefaultHttpDataSourceFactory("exoplayer_video");
            ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();
            MediaSource mediaSource = new ExtractorMediaSource(videoURI, dataSourceFactory, extractorsFactory, null, null);

            exoPlayerView.setPlayer(exoPlayer);
            exoPlayer.prepare(mediaSource);
            exoPlayer.setPlayWhenReady(true);
            exoPlayerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FILL);


        } catch (Exception e) {
            Log.e("MAinActivity", " exoplayer error " + e.toString());
        }
        findViewById(R.id.btn_settings).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(SearchActivity.this, v);
                //Inflating the Popup using xml file
                popup.getMenuInflater().inflate(R.menu.quality_menu, popup.getMenu());

                //registering popup with OnMenuItemClickListener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        Toast.makeText(SearchActivity.this, "You Clicked : " + item.getTitle(), Toast.LENGTH_SHORT).show();
                        return true;
                    }
                });

                popup.show();


               *//* videoTrackSelectionFactory = new AdaptiveTrackSelection.Factory(bandwidthMeter);
                trackSelector = new DefaultTrackSelector(videoTrackSelectionFactory);

                selector = (MappingTrackSelector) trackSelector;
                MappingTrackSelector.MappedTrackInfo mappedTrackInfo = selector.getCurrentMappedTrackInfo();
                if (mappedTrackInfo != null) {
                    int rendererIndex = 0;


                    Pair<AlertDialog, TrackSelectionView> dialogPair =
                            TrackSelectionView.getDialog(SearchActivity.this, "Available Quality", (DefaultTrackSelector) selector, rendererIndex);
                    dialogPair.first.show();
                }*//*


            }
        });*/


    }

    @Override
    protected void onResume() {
        super.onResume();


    }



}


/*public class videoSwitch extends AppCompatActivity {
    private PlayerView simpleExoPlayerView;
    private SimpleExoPlayer player;
    private View loading;
    private ImageButton changeQuality;
    private TrackGroupArray trackGroups;
    private TrackSelector trackSelector;
    private MappingTrackSelector selector;
    private TrackSelection.Factory videoTrackSelectionFactory;
    String space = "\n";
    Uri URL;
    private DefaultBandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN); //will hide the status bar
        setContentView(R.layout.activity_video_switch);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE); //will rotate the screen
        Bundle b = getIntent().getExtras();
        String playable = b.getString("playable", "");
        String url = b.getString("address_", "");
        String name = b.getString("name_", "");
        String address = ("http://" + name + "/" + url);

        if (playable.equals("yes")) {
            URL = Uri.parse(address);
            // Toast.makeText(getBaseContext(), address, Toast.LENGTH_LONG).show();
            initializePlayer(bandwidthMeter);
        } else {
            Toast.makeText(getBaseContext(), "Address is empty or not Valid! Playing a pre-defined Url", Toast.LENGTH_LONG).show();
            //  Toast.makeText(getBaseContext(), "Player launched!", Toast.LENGTH_LONG).show();
            URL = Uri.parse("https://s3-us-west-2.amazonaws.com/hls-playground/hls.m3u8");//multiple quality
            //URL = Uri.parse("https://bitdash-a.akamaihd.net/content/MI201109210084_1/m3u8s/f08e80da-bf1d-4e3d-8899-f0f6155f6efa.m3u8");
            //URL = Uri.parse("https://bitdash-a.akamaihd.net/content/sintel/hls/playlist.m3u8");
            // URL = Uri.parse("http://115.112.70.86/new.m3u8");
            //URL = Uri.parse("http://cbsnewshd-lh.akamaihd.net/i/CBSNHD_7@199302/index_700_av-p.m3u8");
            initializePlayer(bandwidthMeter);
        }

        // Toast.makeText(getBaseContext(), "Player launched!", Toast.LENGTH_LONG).show();
        player.addListener(new ExoPlayer.EventListener() {

            @Override
            public void onTimelineChanged(Timeline timeline, Object manifest, int reason) {
            }

            @Override
            public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {
                Log.d("_____________________ ", String.valueOf(space));

            }

            @Override
            public void onLoadingChanged(boolean isLoading) {
            }

            @Override
            public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
                switch (playbackState) {
                    case ExoPlayer.STATE_READY:
                        loading.setVisibility(View.GONE);
                        break;
                    case ExoPlayer.STATE_BUFFERING:
                        loading.setVisibility(View.VISIBLE);
                        break;
                }
                if (playbackState == Player.STATE_IDLE || playbackState == Player.STATE_ENDED ||
                        !playWhenReady) {

                    simpleExoPlayerView.setKeepScreenOn(false);
                } else {
                    // This prevents the screen from getting dim/lock
                    simpleExoPlayerView.setKeepScreenOn(true);
                }
            }

            @Override
            public void onRepeatModeChanged(int repeatMode) {

            }


            @Override
            public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {
            }

            @Override
            public void onPlayerError(ExoPlaybackException error) {
                Toast.makeText(getBaseContext(), "Error Occurred While Fetching the Url,Please check the Url ", Toast.LENGTH_LONG).show();
                player.stop();
                player.setPlayWhenReady(true);
                initializePlayer(bandwidthMeter);
            }

            @Override
            public void onPositionDiscontinuity(int reason) {
            }

            @Override
            public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {
            }

            @Override
            public void onSeekProcessed() {
            }

        });
        player.setPlayWhenReady(true); //run file/link when ready to play.
    }

    private void initializePlayer(DefaultBandwidthMeter bandwidthMeter) {
        loading = findViewById(R.id.loading);
        changeQuality = findViewById(R.id.change_quality);
        videoTrackSelectionFactory = new AdaptiveTrackSelection.Factory(bandwidthMeter);
        trackSelector = new DefaultTrackSelector(videoTrackSelectionFactory);
        player = ExoPlayerFactory.newSimpleInstance(this, trackSelector);
        selector = (MappingTrackSelector) trackSelector;
        simpleExoPlayerView = new SimpleExoPlayerView(this);
        simpleExoPlayerView = (SimpleExoPlayerView) findViewById(R.id.player_view);
        simpleExoPlayerView.setUseController(true);//set to true or false to see controllers
        simpleExoPlayerView.requestFocus();
        simpleExoPlayerView.setPlayer(player);
        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(this, Util.getUserAgent(this, "StreamTest"), bandwidthMeter);
        MediaSource videoSource = new HlsMediaSource(URL, dataSourceFactory, 1, null, null);
        player.prepare(videoSource);
        changeQuality.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                change_Quality();
            }
        });
    }


    private void change_Quality() {

        MappingTrackSelector.MappedTrackInfo mappedTrackInfo = selector.getCurrentMappedTrackInfo();
        if (mappedTrackInfo != null) {
            int rendererIndex = 0;


            Pair<AlertDialog, TrackSelectionView> dialogPair =
                    TrackSelectionView.getDialog(videoSwitch.this, "Available Quality", (DefaultTrackSelector) selector, rendererIndex);
            dialogPair.first.show();
        }
    }

    private void hideSystemUi() {
        simpleExoPlayerView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
    }

    @Override
    protected void onStop() {
        super.onStop();
        player.release();

    }


    @Override
    protected void onStart() {
        super.onStart();

    }


    @Override
    protected void onResume() {
        super.onResume();
        hideSystemUi();
    }

    @Override
    protected void onPause() {
        super.onPause();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        player.release();
    }
}*/
