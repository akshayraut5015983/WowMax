package com.swaliya.wowmax.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.InstallState;
import com.google.android.play.core.install.InstallStateUpdatedListener;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.InstallStatus;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.google.android.play.core.tasks.OnSuccessListener;
import com.google.android.play.core.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;
import com.swaliya.wowmax.BuildConfig;
import com.swaliya.wowmax.R;
import com.swaliya.wowmax.configg.SessionManager;
import com.swaliya.wowmax.helper.VersionChecker;

import java.io.File;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

import pl.droidsonroids.gif.GifImageView;


public class SplashActivity extends Activity {
    SessionManager session;
    Animation anim;
    ImageView imageView;
    FirebaseAuth mAuth;
    private int MY_REQUEST_CODE = 11;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        mAuth = FirebaseAuth.getInstance();
        session = new SessionManager(SplashActivity.this);
        imageView = (ImageView) findViewById(R.id.img);
        // anim = AnimationUtils.loadAnimation(SplashActivity.this, R.anim.zoom);

        //  imageView.startAnimation(anim);

        VideoView videoView = (VideoView) findViewById(R.id.vv);
//        MediaController mediaController = new MediaController(this);
//        mediaController.setAnchorView(videoView);
        Uri uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.spl);
        // videoView.setMediaController(mediaController);
        videoView.setVideoURI(uri);
        videoView.requestFocus();
        videoView.setZOrderOnTop(true);
        videoView.start();
        deleteCache(this);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mAuth.getCurrentUser() != null) {
                    mAuth.signOut();
                    startActivity(new Intent(SplashActivity.this, LoginActivity.class).putExtra("key", "mail"));
                    finish();
                } else if (session.isLoggedIn()) {
                    startActivity(new Intent(SplashActivity.this, MainActivity.class).putExtra("key", "reg").setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                    finish();
                } else {
                    startActivity(new Intent(SplashActivity.this, LoginActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                    finish();
                }

            }
        }, 4000);

      /*  PackageInfo info = null;
        try {
            PackageManager manager = this.getPackageManager();
            info = null;

            info = manager.getPackageInfo(
                    this.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        String version = info.versionName;
        Log.d("TAG", "onCreate: " + version);*/

        /*VersionChecker versionChecker = new VersionChecker();
        String mLatestVersionName = "";
        try {
            mLatestVersionName = versionChecker.execute().get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        Log.d("TAG", mLatestVersionName + "  latest  " + BuildConfig.VERSION_NAME);
        try {
            if (Double.parseDouble(BuildConfig.VERSION_NAME) < Double.parseDouble(mLatestVersionName)) {

                Log.d("TAG", "Greter Version: ");
            } else {
                Log.d("TAG", "samller vesion: ");
            }
        } catch (NumberFormatException e) {
            Log.d("TAG", "NumberFormatException: " + e.getMessage());
        }*/

       /* VersionChecker versionChecker = new VersionChecker();
        try {
            String latestVersion = versionChecker.execute().get();
            String versionName = BuildConfig.VERSION_NAME.replace("-DEBUG", "");
            if (latestVersion != null && !latestVersion.isEmpty()) {
                if (!latestVersion.equals(versionName)) {
                    showDialogToSendToPlayStore();
                } else {
                    recreate();
                }
            }
            //  Log.d("update", "Current version " + Float.valueOf(versionName) + ", Playstore version " + Float.valueOf(latestVersion));

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        final AppUpdateManager appUpdateManager = AppUpdateManagerFactory.create(this);
        Task<AppUpdateInfo> appUpdateInfoTask = appUpdateManager.getAppUpdateInfo();
        appUpdateInfoTask.addOnSuccessListener(new OnSuccessListener<AppUpdateInfo>() {
            @Override
            public void onSuccess(AppUpdateInfo appUpdateInfo) {
                if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                        && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)) {

                    try {
                        appUpdateManager.startUpdateFlowForResult(appUpdateInfo, AppUpdateType.IMMEDIATE, SplashActivity.this, MY_REQUEST_CODE);
                    } catch (IntentSender.SendIntentException e) {
                        e.printStackTrace();
                        Log.d("TAG", "onSuccess : " + e.getMessage());
                    }
                }
            }
        });*/
    }


    private void showDialogToSendToPlayStore() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Update Available You Want To Update");
        builder.setCancelable(false);
        builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

            }
        });
        builder.setNegativeButton("Close", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finishAffinity();
            }
        });
        Dialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == MY_REQUEST_CODE) {
            Toast.makeText(this, "start dwm", Toast.LENGTH_SHORT).show();
            if (resultCode != RESULT_OK) {
                Log.d("TAG", "Update flow failed! Result code: " + resultCode);
            }
        }
    }

    protected boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnected()) {
            return true;
        } else {
            return false;
        }
    }

    public static void printHashKey(Context pContext) {
        try {
            PackageInfo info = pContext.getPackageManager().getPackageInfo(pContext.getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String hashKey = new String(Base64.encode(md.digest(), 0));
                Log.i("TAG", "printHashKey() Hash Key: " + hashKey);
            }
        } catch (NoSuchAlgorithmException e) {
            Log.e("TAG", "printHashKey()", e);
        } catch (Exception e) {
            Log.e("TAG", "printHashKey()", e);
        }
    }


    public static void deleteCache(Context context) {
        try {
            File dir = context.getCacheDir();
            deleteDir(dir);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
            return dir.delete();
        } else if (dir != null && dir.isFile()) {
            return dir.delete();
        } else {
            return false;
        }
    }
}
