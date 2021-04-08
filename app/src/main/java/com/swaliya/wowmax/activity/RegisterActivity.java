package com.swaliya.wowmax.activity;

import android.animation.ValueAnimator;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.swaliya.wowmax.R;
import com.swaliya.wowmax.configg.Config;
import com.swaliya.wowmax.configg.SessionManager;

import java.util.Arrays;


public class RegisterActivity extends AppCompatActivity {
    EditText edName, edMob, edEmail, edPass;
    String strName = "", strMob = "", strEmail = "", strPass = "";

    SessionManager sessionManager;
    private ProgressDialog pDialog;
    String urlMain = "";

    private static final int RC_SIGN_IN = 234;

    //Tag for the logs optional
    private static final String TAG = "coding";

    //creating a GoogleSignInClient object
    GoogleSignInClient mGoogleSignInClient;
    CallbackManager mCallbackManager;
    FirebaseAuth mAuth;
    Dialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        mAuth = FirebaseAuth.getInstance();
        mCallbackManager = CallbackManager.Factory.create();
        sessionManager = new SessionManager(getApplicationContext());
        edName = findViewById(R.id.edName);
        edMob = findViewById(R.id.edMob);
        edEmail = findViewById(R.id.edEmail);
        edPass = findViewById(R.id.edPass);
        dialog = new Dialog(this);

        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                finish();
            }
        });
        findViewById(R.id.btnRegister).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                strName = edName.getText().toString();
                strEmail = edEmail.getText().toString();
                strMob = edMob.getText().toString();
                strPass = edPass.getText().toString();
                String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

                if (strName.equals("")) {
                    Toast.makeText(RegisterActivity.this, "Enter name", Toast.LENGTH_SHORT).show();
                } else if (strEmail.equals("")) {
                    Toast.makeText(RegisterActivity.this, "Enter email", Toast.LENGTH_SHORT).show();
                } else if (!strEmail.matches(emailPattern)) {
                    Toast.makeText(RegisterActivity.this, "Enter valid email", Toast.LENGTH_SHORT).show();
                } else if (strMob.equals("")) {
                    Toast.makeText(RegisterActivity.this, "Enter mobile", Toast.LENGTH_SHORT).show();
                } else if (strMob.length() != 10) {
                    Toast.makeText(RegisterActivity.this, "Enter valid mobile", Toast.LENGTH_SHORT).show();
                } else if (strPass.equals("")) {
                    Toast.makeText(RegisterActivity.this, "Enter password", Toast.LENGTH_SHORT).show();
                } else {
                    String name = strName.replaceAll(" ", "_");
                    ConnectivityManager cn = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                    NetworkInfo nf = cn.getActiveNetworkInfo();
                    if (nf != null && nf.isConnected() == true) {
                        // http://demo1.swaliyasoftech.com/API/APIURL.aspx?msg=SelectMobile%207823089320
                        String url = Config.URL + "API/APIURL.aspx?msg=SelectMobile%20" + strMob;
                        // getDataMobie(url);
                        pDialog = new ProgressDialog(RegisterActivity.this);
                        pDialog.setMessage("Please Wait Verifing mobile...");
                        pDialog.setCancelable(false);
                        pDialog.show();
                        //  http://demo1.swaliyasoftech.com/API/APIURL.aspx?msg=InsertMemberRecord%20Vaishali%2080024803%20test@gmail.com%20456123
                        // http://wowmaxmovies.com/API/APIURL.aspx?msg=newuser%20name%20city%20email%20mobile%20subscription%20700%20pass
                        urlMain = Config.URL + "API/APIURL.aspx?msg=newuser%20" + name + "%20city%20" + strEmail + "%20" + strMob + "%20subscription%20700%20" + strPass;
                        Log.d(TAG, "url: " + urlMain);
                        getData(urlMain, strName, strEmail, strMob);
                        //  Toast.makeText(RegisterActivity.this, "Registration Successfully", Toast.LENGTH_LONG).show();

                    } else {
                        Toast.makeText(RegisterActivity.this, "Check internet connection", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
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
        findViewById(R.id.fb).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                LoginManager.getInstance().logInWithReadPermissions(RegisterActivity.this, Arrays.asList("email", "public_profile"));
                LoginManager.getInstance().registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        Log.d("Tag", "facebook:onSuccess:" + loginResult);
                        Toast.makeText(RegisterActivity.this, "Wait a minutes", Toast.LENGTH_SHORT).show();
                        handleFacebookAccessToken(loginResult.getAccessToken());
                    }

                    @Override
                    public void onCancel() {
                        Log.d("Tag", "facebook:onCancel");
                        Toast.makeText(RegisterActivity.this, "Cancel login", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(FacebookException error) {
                        Log.d("Tag", "facebook:onError");
                        Toast.makeText(RegisterActivity.this, "Error", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

// ...
    }

    public void handleFacebookAccessToken(AccessToken token) {
        Log.d("TAg", "handleFacebookAccessToken:" + token);
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential).addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {

                    Log.d("TAg", "signInWithCredential:success");
                    FirebaseUser user = mAuth.getCurrentUser();


                    fromGamil(user.getDisplayName(), user.getEmail(), user.getPhoneNumber());

//                    startActivity(new Intent(getApplicationContext(), MainActivity.class).putExtra("key", "mail"));
//                    sessionManager.createLoginSession(user.getDisplayName(), user.getEmail(), user.getPhoneNumber(), "");
//                    Toast.makeText(RegisterActivity.this, "Welcome", Toast.LENGTH_SHORT).show();
                } else {

                    Toast.makeText(RegisterActivity.this, "Authentication failed. Error in Network",
                            Toast.LENGTH_SHORT).show();

                }
            }
        });

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
                    //        Toast.makeText(RegisterActivity.this, "Welcome", Toast.LENGTH_SHORT).show();
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            fromGamil(user.getDisplayName(), user.getEmail(), user.getPhoneNumber());

//                            sessionManager.createLoginSession(user.getDisplayName(), user.getEmail(), user.getPhoneNumber(), "");
//                            startActivity(new Intent(RegisterActivity.this, MainActivity.class).putExtra("key", "mail"));
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(RegisterActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();

                        }

                        // ...
                    }
                });
    }

    private void checkPassword() {


    }

    private void fromGamil(String name, String email, String mobile) {

        Log.d(TAG, "makeText: " + name + " " + email + " " + mobile);

        dialog.setContentView(R.layout.layout_mail_pass);

        final EditText edName = (EditText) dialog.findViewById(R.id.edName);
        final EditText edemail = (EditText) dialog.findViewById(R.id.edEmail);
        final EditText edmob = (EditText) dialog.findViewById(R.id.edMob);
        final EditText edpass = (EditText) dialog.findViewById(R.id.edPass);
        edName.setText(name);
        edemail.setText(email);
        edmob.setText(mobile);


        final Button dialogButton = (Button) dialog.findViewById(R.id.btnRegister);
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                strName = edName.getText().toString();
                strEmail = edemail.getText().toString();
                strMob = edmob.getText().toString();
                strPass = edpass.getText().toString();
                String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

                if (strName.equals("")) {
                    Toast.makeText(RegisterActivity.this, "Enter name", Toast.LENGTH_SHORT).show();
                } else if (strEmail.equals("")) {
                    Toast.makeText(RegisterActivity.this, "Enter email", Toast.LENGTH_SHORT).show();
                } else if (!strEmail.matches(emailPattern)) {
                    Toast.makeText(RegisterActivity.this, "Enter valid email", Toast.LENGTH_SHORT).show();
                } else if (strMob.equals("")) {
                    Toast.makeText(RegisterActivity.this, "Enter mobile", Toast.LENGTH_SHORT).show();
                } else if (strMob.length() != 10) {
                    Toast.makeText(RegisterActivity.this, "Enter valid mobile", Toast.LENGTH_SHORT).show();
                } else if (strPass.equals("")) {
                    Toast.makeText(RegisterActivity.this, "Enter password", Toast.LENGTH_SHORT).show();
                } else {
                    String name = strName.replaceAll(" ", "_");
                    ConnectivityManager cn = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                    NetworkInfo nf = cn.getActiveNetworkInfo();
                    if (nf != null && nf.isConnected() == true) {
                        String url = Config.URL + "API/APIURL.aspx?msg=SelectMobile%20" + strMob;
                        pDialog = new ProgressDialog(RegisterActivity.this);
                        pDialog.setMessage("Please Wait Verifing mobile...");
                        pDialog.setCancelable(false);
                        pDialog.show();
                        urlMain = Config.URL + "API/APIURL.aspx?msg=newuser%20" + name + "%20city%20" + strEmail + "%20" + strMob + "%20subscription%20700%20" + strPass;
                        Log.d(TAG, "url: " + urlMain);
                        getData(urlMain, strName, strEmail, strMob);

                    } else {
                        Toast.makeText(RegisterActivity.this, "Check internet connection", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        dialog.show();


    }

    private void signIn() {
        //getting the google signin intent
        //  mGoogleSignInClient.signOut();
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        //starting the activity for result
        startActivityForResult(signInIntent, RC_SIGN_IN);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Toast.makeText(this, "Wait a minutes..", Toast.LENGTH_SHORT).show();

        if (requestCode == RC_SIGN_IN) {
            //Getting the GoogleSignIn Task
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                //Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                //authenticating with firebase
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                Log.d(TAG, "getStatusCode: " + e.getStatusCode());
                Log.d(TAG, "getLocalizedMessage: " + e.getLocalizedMessage());
                Log.d(TAG, "getMessage: " + e.getMessage());
                Toast.makeText(RegisterActivity.this, "Error In  network Please...!", Toast.LENGTH_LONG).show();
            }
        }
        // Pass the activity result back to the Facebook SDK
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }

   /* private void getDataMobie(String url) {
        StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                pDialog.dismiss();
                if (response.contains("invalid")) {
                    getData(urlMain);
                    pDialog = new ProgressDialog(RegisterActivity.this);
                    pDialog.setMessage("Please Wait Registering Processing...");
                    pDialog.setCancelable(false);
                    pDialog.show();
                } else {
                    edMob.setText("");
                    Toast.makeText(RegisterActivity.this, "Mobile number Already Registered", Toast.LENGTH_LONG).show();

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pDialog.dismiss();
                Toast.makeText(RegisterActivity.this, error.getMessage() + "", Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    } */

    private void getData(String url, final String name, final String Email, final String Mob) {
        StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                pDialog.dismiss();
                if (response.contains("Data Exists")) {
                    Toast.makeText(RegisterActivity.this, "Data  already exist", Toast.LENGTH_LONG).show();
                    edEmail.setText("");
                    edMob.setText("");
                    edPass.setText("");
                    edName.setText("");
                    if (dialog.isShowing()) {
                        dialog.dismiss();
                    }
                } else if (response.contains("successfully")) {
                    Toast.makeText(RegisterActivity.this, "Login Successfully", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                    intent.putExtra("key", "reg");
                    startActivity(intent);
                    sessionManager.createLoginSession(name, Email, Mob, "strPass");
                    finish();
                    if (dialog.isShowing()) {
                        dialog.dismiss();
                    }
                } else {
                    if (dialog.isShowing()) {
                        dialog.dismiss();
                    }
                    Toast.makeText(RegisterActivity.this, "Something wrong in network  ", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pDialog.dismiss();
                Toast.makeText(RegisterActivity.this, error.getMessage() + "", Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }

    // ₹ """  "\u20B9 "
}
