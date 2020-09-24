package com.swaliya.vidmax.activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.swaliya.vidmax.R;
import com.swaliya.vidmax.configg.SessionManager;

public class LoginActivity extends AppCompatActivity /*implements GoogleApiClient.OnConnectionFailedListener*/ {

   /* private static final String TAG = "MainActivity";
    private SignInButton signInButton;
    private GoogleApiClient googleApiClient;
    private static final int RC_SIGN_IN = 1;
    String name, email;
    String idToken;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;*/

    private static final int RC_SIGN_IN = 234;

    //Tag for the logs optional
    private static final String TAG = "coding";

    //creating a GoogleSignInClient object
    GoogleSignInClient mGoogleSignInClient;

    //And also a Firebase Auth object
    FirebaseAuth mAuth;

    EditText edMobile, edPass;
    String strMob = "", strPass = "";
    ImageView mImg, eImg;
    SessionManager sessionManager;
    private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        edMobile = findViewById(R.id.edMob);

        eImg = findViewById(R.id.eimg);
        mImg = findViewById(R.id.mimg);
        sessionManager = new SessionManager(getApplicationContext());
        edPass = findViewById(R.id.edPass);
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


        findViewById(R.id.btnRegister).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), RegisterActivity.class));
            }
        });

        findViewById(R.id.btnLlogin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* strMob = edMobile.getText().toString();
                strPass = edPass.getText().toString();
                if (strMob.equals("")) {
                    Toast.makeText(LoginActivity.this, "Enter mobile number", Toast.LENGTH_SHORT).show();
                } else if (strMob.length() != 10) {
                    Toast.makeText(LoginActivity.this, "Enter the valid mobile number", Toast.LENGTH_SHORT).show();
                } else if (strPass.equals("")) {
                    Toast.makeText(LoginActivity.this, "Enter password", Toast.LENGTH_SHORT).show();
                } else {
                    // doLogin(strMob, strPass);
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    sessionManager.createLoginSession("name", strMob, strPass);
                }*/
                startActivity(new Intent(getApplicationContext(), MainActivity.class).putExtra("key", "reg"));
                sessionManager.createLoginSession("Vidmax", "9999999990", "email@yahoo.com");
                finish();
            }
        });

        /* private void doLogin(String strMob, String strPass) {
         *//* startActivity(new Intent(getApplicationContext(), MainActivity.class));
        sessionManager.createLoginSession("name", strMob, strPass);
*//*
        ConnectivityManager cn = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo nf = cn.getActiveNetworkInfo();
        if (nf != null && nf.isConnected() == true) {
            // http://urbsfhelp.live/API/APIlogin.aspx?uid=sai&pass=123&mbl=123456789
            //  http://demo1.swaliyasoftech.com/API/APIURL.aspx?msg=Login%207823089320%20pass123
            String url = Config.URL + "API/APIURL.aspx?msg=Login%20" + strMob + "%20" + strPass;
            getData(url);
            pDialog = new ProgressDialog(LoginActivity.this);
            pDialog.setMessage("Please Wait Login Processing...");
            pDialog.setCancelable(false);
            pDialog.show();

        } else {
            Toast.makeText(this, "Check internet connection", Toast.LENGTH_SHORT).show();
        }
    }

    private void getData(String url) {
        StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.contains("Welcome")) {
                    pDialog.dismiss();
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    sessionManager.createLoginSession("name", strMob, strPass);
                } else if (response.contains("Please Register")){
                    pDialog.dismiss();
                    edMobile.setText("");
                    edPass.setText("");
                    Toast.makeText(LoginActivity.this,  " Mobile and Password Does Not Match", Toast.LENGTH_SHORT).show();
                }else {
                    pDialog.dismiss();
                    edMobile.setText("");
                    edPass.setText("");
                    Toast.makeText(LoginActivity.this,  response.toString(), Toast.LENGTH_SHORT).show();

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pDialog.dismiss();
                Toast.makeText(LoginActivity.this, error.getMessage() + "", Toast.LENGTH_SHORT).show();
            }
        });
      *//*  JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Toast.makeText(LoginActivity.this, response.toString() + "", Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(LoginActivity.this, error.getMessage() + "", Toast.LENGTH_SHORT).show();
            }
        });*//*
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }*/


        mAuth = FirebaseAuth.getInstance();

        //Then we need a GoogleSignInOptions object
        //And we need to build it as below
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        //Then we will get the GoogleSignInClient object from GoogleSignIn class
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        //Now we will attach a click listener to the sign_in_button
        //and inside onClick() method we are calling the signIn() method that will open
        //google sign in intent
        findViewById(R.id.sign_in_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        //if the user is already signed in
        //we will close this activity
        //and take the user to profile activity
        if (mAuth.getCurrentUser() != null) {
            finish();
            startActivity(new Intent(this, MainActivity.class).putExtra("key", "mail"));
        }

        if (sessionManager.isLoggedIn() == true) {
            startActivity(new Intent(getApplicationContext(), MainActivity.class).putExtra("key", "reg"));

            //  overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            finish();
        }

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //if the requestCode is the Google Sign In code that we defined at starting
        if (requestCode == RC_SIGN_IN) {

            //Getting the GoogleSignIn Task
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                //Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);

                //authenticating with firebase
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());

        //getting the auth credential
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);

        //Now using firebase we are signing in the user here
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            sessionManager.createLoginSession(user.getDisplayName(), user.getPhoneNumber(), user.getEmail());
                            startActivity(new Intent(LoginActivity.this, MainActivity.class).putExtra("key", "mail"));
                            Toast.makeText(LoginActivity.this, "Welcome", Toast.LENGTH_SHORT).show();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();

                        }

                        // ...
                    }
                });
    }


    //this method is called on click
    private void signIn() {
        //getting the google signin intent
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();

        //starting the activity for result
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }


    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you want to close");
        builder.setPositiveButton("Register", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                startActivity(new Intent(getApplicationContext(), RegisterActivity.class));


            }
        });
        builder.setNegativeButton("Close", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    finishAffinity();
                }
            }
        });
        Dialog dialog = builder.create();
        dialog.show();
    }

}
