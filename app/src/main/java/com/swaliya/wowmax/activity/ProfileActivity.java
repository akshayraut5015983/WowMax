package com.swaliya.wowmax.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.swaliya.wowmax.R;
import com.swaliya.wowmax.configg.Config;
import com.swaliya.wowmax.configg.SessionManager;

import de.hdodenhof.circleimageview.CircleImageView;

/*
import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.firebase.auth.FirebaseAuth;*/

public class ProfileActivity extends AppCompatActivity/* implements GoogleApiClient.OnConnectionFailedListener*/ {
    Button logoutBtn;

    /* private GoogleApiClient googleApiClient;
     private GoogleSignInOptions gso;*/
    CircleImageView imageView;
    TextView textName, textEmail, textMobile;
    FirebaseAuth mAuth;
    SessionManager sessionManager;
    SharedPreferences pref;
    String loginid = "", mobilenumber = "", passwords = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        findViewById(R.id.imgBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        sessionManager = new SessionManager(this);
        logoutBtn = findViewById(R.id.btnProf);
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

        findViewById(R.id.btnChange).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                forForgotPassword();

            }
        });
       /* gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        googleApiClient = new GoogleApiClient.Builder( this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();


        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(
                        new ResultCallback<Status>() {
                            @Override
                            public void onResult(Status status) {
                                if (status.isSuccess()) {
                                    gotoMainActivity();
                                } else {
                                    Toast.makeText(getApplicationContext(), "Session not close", Toast.LENGTH_LONG).show();
                                }
                            }
                        });
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(googleApiClient);
        if (opr.isDone()) {
            GoogleSignInResult result = opr.get();
            handleSignInResult(result);
        } else {
            opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                @Override
                public void onResult(@NonNull GoogleSignInResult googleSignInResult) {
                    handleSignInResult(googleSignInResult);
                }
            });
        }
    }

    private void handleSignInResult(GoogleSignInResult result) {
        if (result.isSuccess()) {
            GoogleSignInAccount account = result.getSignInAccount();
            userName.setText(account.getDisplayName());
            userEmail.setText(account.getEmail());
            userId.setText(account.getId());
            try {
                Glide.with(this).load(account.getPhotoUrl()).into(profileImage);
            } catch (NullPointerException e) {
                Toast.makeText(getApplicationContext(), "image not found", Toast.LENGTH_LONG).show();
            }

        } else {
            gotoMainActivity();
        }
    }

    private void gotoMainActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
*/
        mAuth = FirebaseAuth.getInstance();

        imageView = findViewById(R.id.imageView);
        textName = findViewById(R.id.textViewName);
        textEmail = findViewById(R.id.textViewEmail);
        textMobile = findViewById(R.id.textViewMobile);


        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            Glide.with(this)
                    .load(user.getPhotoUrl())
                    .into(imageView);

            textName.setText("Name-  " + user.getDisplayName());
            textEmail.setText("Email-  " + user.getEmail());
            textMobile.setText("Mobile-  " + user.getPhoneNumber());
        } else {

            textName.setText("Name-  " + loginid);
            textEmail.setText("Email-  " + mobilenumber);
            textMobile.setText("Mobile-  " + passwords);
        }
       /* imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                mAuth.signOut();
            }

        });*/
        findViewById(R.id.btnProf).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mAuth.getCurrentUser() != null) {
                    FirebaseAuth.getInstance().signOut();
                    LoginManager.getInstance().logOut();
                    startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                    sessionManager.logoutUser();
                    finish();
                } else {
                    startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                    sessionManager.logoutUser();
                    finish();
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        //if the user is not logged in
        //opening the login activity
       /* if (mAuth.getCurrentUser() == null) {
            finish();
            startActivity(new Intent(this, MainActivity.class));
        }*/
    }

    private void forForgotPassword() {

        final Dialog dialog = new Dialog(this, R.style.CustomAlertDialog);
        dialog.setContentView(R.layout.layout_change_pass);
        final EditText edOld = (EditText) dialog.findViewById(R.id.edOldPass);
        final EditText edNew = (EditText) dialog.findViewById(R.id.edNewPass);
        final EditText edConPass = (EditText) dialog.findViewById(R.id.edConPass);

        Button dialogButton = (Button) dialog.findViewById(R.id.btnSubmit);
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String strOldPass = edOld.getText().toString();
                final String strNewPas = edNew.getText().toString();
                final String strConPass = edConPass.getText().toString();
                if (strOldPass.equals("")) {
                    Toast toast = Toast.makeText(ProfileActivity.this, "Enter old Password", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                } else  if (strNewPas.equals("")) {
                    Toast toast = Toast.makeText(ProfileActivity.this, "Enter old New Password", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }else  if (strConPass.equals("")) {
                    Toast toast = Toast.makeText(ProfileActivity.this, "Confirm Password", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }else if (!strNewPas.equals(strConPass)) {
                    edNew.setText("");
                    edConPass.setText("");
                    Toast toast = Toast.makeText(ProfileActivity.this, "Password Not Match", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                } else {

                    overridePendingTransition(R.anim.hold, R.anim.slide_down);
                    dialog.dismiss();
                }
            }
        });

        dialog.show();
    }
}